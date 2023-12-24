package com.finnoapp.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finnoapp.exception.CustomException;
import com.finnoapp.model.Article;
import com.finnoapp.model.Image;
import com.finnoapp.model.User;
import com.finnoapp.payload.request.ArticleRequest;
import com.finnoapp.payload.response.ArticleResponse;
import com.finnoapp.payload.response.GenericMessage;
import com.finnoapp.pojos.ImageUpload;
import com.finnoapp.service.ArticleService;
import com.finnoapp.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private UserService userService;

	@Autowired
	private ImageUpload imageUpload;

	@PostMapping("/create")
	public ResponseEntity<?> createArticle(@ModelAttribute ArticleRequest request) {
		try {
			if (!isValidArticleRequest(request)) {
				return ResponseEntity.badRequest().body(new GenericMessage<>("success", "Invalid request", false));
			}

			Long userId = request.getUserId();
			User user = userService.getUserById(userId);

			if (user == null) {
				throw new CustomException("User Id does not exist in our database");
			}

			Image image = createImageFromRequest(request);
			Article result;
			if (image != null) {
				Article article = createArticleFromRequest(request, user, image);
				result = articleService.addArticle(article);
			} else {
				Article article = new Article(request.getTitle(), request.getContent(), LocalDateTime.now(), user);

				result = this.articleService.addArticle(article);

			}

			if (result != null) {
				return ResponseEntity.ok().body(new GenericMessage<>("success", "Article added.", true));
			} else {
				throw new CustomException("Article not added.");
			}
		} catch (CustomException ce) {
			throw ce;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private Image createImageFromRequest(ArticleRequest request) {
		Image image = new Image();

		try {
			Map<String, String> imageInfo = imageUpload.saveImage(request.getImage());

//			if ("true".equals(imageInfo.get("imageNotFound"))) {
//				throw new CustomException("Image not provided in the request.");
//			}

			if ("yes".equals(imageInfo.get("serverError"))) {
				throw new CustomException("Error occurred while processing the image.");
			}

			if ("true".equalsIgnoreCase(imageInfo.get("imageNotFound"))) {
				return null;
			}

			if (imageInfo.get("imageName") != null && imageInfo.get("imagePath") != null) {
				image.setImageName(imageInfo.get("imageName"));
				image.setImagePath(imageInfo.get("imagePath"));
			}

		} catch (CustomException ce) {
			throw ce;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("Error processing the image.", e);
		}

		return image;
	}

	private Article createArticleFromRequest(ArticleRequest request, User user, Image image) {
		Article article = new Article(request.getTitle(), request.getContent(), LocalDateTime.now(), user);

		if (image != null) {
			image.setArticle(article);
			List<Image> images = new ArrayList<>();
			images.add(image);
			article.setImages(images);
		}

		return article;
	}

	private boolean isValidArticleRequest(ArticleRequest request) {
		return request != null && request.getContent() != null && request.getTitle() != null
				&& request.getUserId() != null;
	}

	@GetMapping("/get/{articleId}")
	public ResponseEntity<?> getArticle(@PathVariable Long articleId) {
		try {
			Article article = articleService.getArticleById(articleId);

			if (article != null) {

				List<Image> images = article.getImages();

				// Convert images to a format suitable for response (e.g., extract image paths)
				List<String> imagePaths = this.extractImagePaths(images);
				ArticleResponse articleResponse = this.convertArticleToArticleImage(article, imagePaths);

				return ResponseEntity.status(HttpStatus.OK)
						.body(new GenericMessage<>("success", "article fetched successfully..", articleResponse, true));
			} else {
				throw new CustomException("Article not found");
			}
		} catch (CustomException ce) {
			throw ce;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			throw e;
		}
	}

	public ArticleResponse convertArticleToArticleImage(Article article, List<String> imagePaths) {

		String publicationDateFormatted = this.formatLocalDateTime(article.getPublicationDate());
		String updateDateFormatted = this.formatLocalDateTime(article.getUpdatedDate());

		return new ArticleResponse(article.getArticleId(), article.getTitle(), article.getContent(),
				article.getUser().getUserId(), publicationDateFormatted, updateDateFormatted, imagePaths);
	}

	private List<String> extractImagePaths(List<Image> images) {
		return images.stream().map(Image::getImagePath).collect(Collectors.toList());
	}

	@GetMapping("get/all-articles")
	public ResponseEntity<?> getListArticle() {
		try {
			List<Article> allArticles = articleService.getAllArticles();

			if (!allArticles.isEmpty()) {
				List<ArticleResponse> list = allArticles.stream().map(article -> {
					String publicationDateFormatted = this.formatLocalDateTime(article.getPublicationDate());
					String updateDateFormatted = this.formatLocalDateTime(article.getUpdatedDate());

					return new ArticleResponse(article.getArticleId(), article.getTitle(), article.getContent(),
							article.getUser().getUserId(), publicationDateFormatted, updateDateFormatted,
							extractImagePaths(article.getImages()));
				}).collect(Collectors.toList());

				return ResponseEntity.ok(new GenericMessage<>("Article fetched successfully.", list, true));
			} else {
				throw new CustomException("Article not found");
			}
		} catch (CustomException ce) {
			throw ce;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			throw e;
		}
	}

	@DeleteMapping("delete/{articleId}")
	public ResponseEntity<?> deleteArticle(@PathVariable Long articleId) {
		try {
			// Check if the article exists
			Article article = articleService.getArticleById(articleId);

			if (article != null) {
				// Delete the article
				articleService.deleteArticle(articleId);
				return ResponseEntity.ok(new GenericMessage<>("Article deleted successfully.", false));
			} else {
				throw new CustomException("Article not found");
			}
		} catch (CustomException ce) {
			throw ce;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			throw e;
		}
	}

	// update article

	@PutMapping("/update/{articleId}")
	public ResponseEntity<?> updateArticle(@PathVariable Long articleId,
			@RequestBody ArticleRequest updatedArticleRequest) {
		try {
			// Check if the article exists
			Article existingArticle = articleService.getArticleById(articleId);

			if (existingArticle != null) {
				// Update the existing article with the new data
				existingArticle.setTitle(updatedArticleRequest.getTitle());
				existingArticle.setContent(updatedArticleRequest.getContent());
				existingArticle.setUpdatedDate(LocalDateTime.now());

				// Image update

				// Save the updated article
				Article updatedArticle = articleService.updateArticle(existingArticle);

				// formatted the date
				String publicationDateFormatted = this.formatLocalDateTime(updatedArticle.getPublicationDate());
				String updateDateFormatted = this.formatLocalDateTime(updatedArticle.getUpdatedDate());

				ArticleResponse articleResponse = new ArticleResponse(updatedArticle.getArticleId(),
						updatedArticle.getTitle(), updatedArticle.getContent(), updatedArticle.getUser().getUserId(),
						publicationDateFormatted, updateDateFormatted);

				return ResponseEntity.ok(new GenericMessage<>("Article updated successfully.", articleResponse, true));
			} else {
				throw new CustomException("Article not found");
			}
		} catch (CustomException ce) {
			throw ce;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			throw e;
		}
	}

	private String formatLocalDateTime(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return dateTime != null ? dateTime.format(formatter) : "";
	}

}
