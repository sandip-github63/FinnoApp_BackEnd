package com.finnoapp.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finnoapp.config.SecurityConfig;
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

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

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
				logger.warn("Invalid article request received: {}", request);
				return ResponseEntity.badRequest().body(new GenericMessage<>("success", "Invalid request", false));
			}

			Long userId = Long.parseLong(request.getUserId());

			User user = userService.getUserById(userId);
			if (user == null) {
				logger.error("User with ID {} does not exist in our database", request.getUserId());
				throw new CustomException("User Id does not exist in our database");
			}

			Image image = createImageFromRequest(request);
			Article result = (image != null) ? articleService.addArticle(createArticleFromRequest(request, user, image))
					: articleService.addArticle(
							new Article(request.getTitle(), request.getContent(), LocalDateTime.now(), user));

			if (result != null) {
				logger.info("Article added successfully: {}", result);
				return ResponseEntity.ok().body(new GenericMessage<>("success", "Article added.", true));
			} else {
				logger.error("Failed to add the article");
				throw new CustomException("Article not added.");
			}
		} catch (CustomException ce) {
			logger.error("CustomException occurred: {}", ce.getMessage());
			throw ce;
		} catch (Exception e) {
			logger.error("An unexpected error occurred while processing the request", e);
			e.printStackTrace();
			throw e;
		}
	}

	@GetMapping("/get/{articleId}")
	public ResponseEntity<?> getArticle(@PathVariable Long articleId) {
		try {
			Article article = articleService.getArticleById(articleId);

			if (article != null) {

				List<Image> images = article.getImages();

				// Convert images to a format suitable for response (e.g., extract image paths)
				List<String> imageNames = this.extractImageNames(images);
				ArticleResponse articleResponse = this.convertArticleToArticleImage(article, imageNames);

				logger.info("Article with ID {} fetched successfully", articleId);

				return ResponseEntity.ok()
						.body(new GenericMessage<>("success", "article fetched successfully..", articleResponse, true));
			} else {
				logger.warn("Article with ID {} not found", articleId);
				throw new CustomException("Article not found");
			}
		} catch (CustomException ce) {
			logger.error("CustomException occurred: {}", ce.getMessage());
			throw ce;
		} catch (Exception e) {
			logger.error("An unexpected error occurred while fetching the article with ID {}", articleId, e);
			throw e;
		}
	}

	@GetMapping("get/all-articles")
	public ResponseEntity<?> getListArticle() {

		try {

			logger.info("Inside the GetListArticle Method ");
			List<Article> allArticles = articleService.getAllArticles();

			if (!allArticles.isEmpty()) {
				List<ArticleResponse> list = allArticles.stream().map(article -> {
					String publicationDateFormatted = this.formatLocalDateTime(article.getPublicationDate());
					String updateDateFormatted = this.formatLocalDateTime(article.getUpdatedDate());

					return new ArticleResponse(article.getArticleId(), article.getTitle(), article.getContent(),
							article.getUser().getUserId(), publicationDateFormatted, updateDateFormatted,
							extractImageNames(article.getImages()));
				}).collect(Collectors.toList());

				logger.info("List of articles fetched successfully. Count: {}", list.size());

				return ResponseEntity.ok(new GenericMessage<>("Article fetched successfully.", list, true));
			} else {
				logger.warn("No articles found");
				throw new CustomException("Article not found");
			}
		} catch (CustomException ce) {
			logger.error("CustomException occurred: {}", ce.getMessage());
			throw ce;
		} catch (Exception e) {
			logger.error("An unexpected error occurred while fetching the article list", e);
			throw e;
		}
	}

	@DeleteMapping("delete/{articleId}")
	public ResponseEntity<?> deleteArticle(@PathVariable Long articleId) {
		try {
			logger.info("Deleting article with ID: {}", articleId);

			// Check if the article exists
			Article article = articleService.getArticleById(articleId);

			if (article != null) {
				// Delete the associated image from the server if the image name is not null
				article.getImages().stream().findFirst().map(Image::getImageName).filter(imageName -> imageName != null)
						.ifPresent(imageName -> {
							imageUpload.deleteImageFromServerByImageName(imageName);
							logger.info("Associated image deleted for article with ID: {}", articleId);
						});

				// Delete the article
				articleService.deleteArticle(articleId);

				logger.info("Article with ID {} deleted successfully", articleId);

				return ResponseEntity.ok(new GenericMessage<>("success", "Article deleted successfully.", true));
			} else {
				logger.warn("Article with ID {} not found", articleId);
				throw new CustomException("Article not found");
			}
		} catch (CustomException ce) {
			logger.error("CustomException occurred: {}", ce.getMessage());
			throw ce;
		} catch (Exception e) {
			logger.error("An unexpected error occurred while deleting the article with ID {}", articleId, e);
			throw e;
		}
	}

	@PutMapping("/update/{articleId}")
	public ResponseEntity<?> updateArticle(@PathVariable Long articleId,
			@ModelAttribute ArticleRequest updatedArticleRequest) {
		try {
			logger.info("Updating article with ID: {}", articleId);

			Article existingArticle = articleService.getArticleById(articleId);

			if (existingArticle != null) {
				updateArticleDetails(existingArticle, updatedArticleRequest);

				if (updatedArticleRequest.getImage() != null) {
					Image updatedImage = updateArticleImage(existingArticle.getImages().get(0), updatedArticleRequest);

					if (updatedImage != null) {
						List<Image> updatedImages = new ArrayList<>();
						updatedImages.add(updatedImage);
						existingArticle.setImages(updatedImages);
					}
				}

				Article updatedArticle = articleService.updateArticle(existingArticle);

				ArticleResponse articleResponse = convertArticleToArticleImage(updatedArticle);

				logger.info("Article with ID {} updated successfully", articleId);

				return ResponseEntity
						.ok(new GenericMessage<>("success", "Article updated successfully.", articleResponse, true));
			} else {
				logger.warn("Article with ID {} not found", articleId);
				throw new CustomException("Article not found");
			}
		} catch (CustomException ce) {
			logger.error("CustomException occurred: {}", ce.getMessage());
			throw ce;
		} catch (Exception e) {
			logger.error("An unexpected error occurred while updating the article with ID {}", articleId, e);
			throw e;
		}
	}

	@GetMapping("get/latest-articles")
	public ResponseEntity<?> getLatestArticles(@RequestParam(defaultValue = "10") int count, Pageable pageable) {
		try {
			logger.info("Inside the GetLatestArticles Method ");

			// Update the page size in the Pageable object based on the count parameter
			pageable = PageRequest.of(0, count, Sort.by(Sort.Direction.DESC, "publicationDate"));

			Page<Article> latestArticlesPage = articleService.getLatestArticles(pageable);

			List<ArticleResponse> list = latestArticlesPage.getContent().stream().map(article -> {
				String publicationDateFormatted = this.formatLocalDateTime(article.getPublicationDate());
				String updateDateFormatted = this.formatLocalDateTime(article.getUpdatedDate());

				return new ArticleResponse(article.getArticleId(), article.getTitle(), article.getContent(),
						article.getUser().getUserId(), publicationDateFormatted, updateDateFormatted,
						extractImageNames(article.getImages()));
			}).collect(Collectors.toList());

			if (!list.isEmpty()) {
				logger.info("List of latest articles fetched successfully. Count: {}", list.size());
				return ResponseEntity.ok(new GenericMessage<>("Latest articles fetched successfully.", list, true));
			} else {
				logger.warn("No latest articles found");
				throw new CustomException("Latest articles not found");
			}
		} catch (CustomException ce) {
			logger.error("CustomException occurred: {}", ce.getMessage());
			throw ce;
		} catch (Exception e) {
			logger.error("An unexpected error occurred while fetching the latest articles", e);
			throw e;
		}
	}

	@GetMapping("get/user-articles/{userId}")
	public ResponseEntity<?> getUserArticles(@PathVariable Long userId) {
		try {
			logger.info("Inside the getUserArticles Method for user ID: {}", userId);

			List<Article> articles = articleService.getUserArticles(userId);

			List<ArticleResponse> list = articles.stream().map(article -> {
				String publicationDateFormatted = this.formatLocalDateTimeAndMinutes(article.getPublicationDate());
				String updateDateFormatted = this.formatLocalDateTimeAndMinutes(article.getUpdatedDate());

				return new ArticleResponse(article.getArticleId(), article.getTitle(), article.getContent(),
						article.getUser().getUserId(), publicationDateFormatted, updateDateFormatted,
						extractImageNames(article.getImages()));
			}).collect(Collectors.toList());

			if (!list.isEmpty()) {
				logger.info("List of user articles fetched successfully. Count: {}", list.size());
				return ResponseEntity.ok(new GenericMessage<>("User articles fetched successfully.", list, true));
			} else {
				logger.warn("No articles found for user ID: {}", userId);
				throw new CustomException("Articles not found for the specified user");
			}
		} catch (CustomException ce) {
			logger.error("CustomException occurred: {}", ce.getMessage());
			throw ce;
		} catch (Exception e) {
			logger.error("An unexpected error occurred while fetching user articles", e);
			throw e;
		}
	}

	public ArticleResponse convertArticleToArticleImage(Article article, List<String> imagePaths) {
		try {
			logger.debug("Converting Article to ArticleResponse: {}", article);

			String publicationDateFormatted = this.formatLocalDateTime(article.getPublicationDate());
			String updateDateFormatted = this.formatLocalDateTime(article.getUpdatedDate());

			ArticleResponse articleResponse = new ArticleResponse(article.getArticleId(), article.getTitle(),
					article.getContent(), article.getUser().getUserId(), publicationDateFormatted, updateDateFormatted,
					imagePaths);

			logger.debug("Conversion successful. ArticleResponse: {}", articleResponse);

			return articleResponse;
		} catch (Exception e) {
			logger.error("Error occurred while converting Article to ArticleResponse", e);
			throw e;
		}
	}

	private List<String> extractImagePaths(List<Image> images) {
		try {
			logger.debug("Extracting image paths from {} images", images.size());

			List<String> imagePaths = images.stream().map(Image::getImagePath).collect(Collectors.toList());

			logger.debug("Image paths extracted successfully: {}", imagePaths);

			return imagePaths;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error occurred while extracting image paths", e);
			throw e;
		}
	}

	private List<String> extractImageNames(List<Image> images) {
		try {
			logger.debug("Extracting image Name from {} images", images.size());

			List<String> imageNames = images.stream().map(Image::getImageName).collect(Collectors.toList());

			logger.debug("Image paths extracted successfully: {}", imageNames);

			return imageNames;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error occurred while extracting image paths", e);
			throw e;
		}
	}

	private Image createImageFromRequest(ArticleRequest request) {
		Image image = new Image();

		try {
			logger.debug("Creating image from request");

			Map<String, String> imageInfo = imageUpload.saveImage(request.getImage());

			if ("yes".equals(imageInfo.get("serverError"))) {
				logger.error("Error occurred while processing the image. Request: {}", request);
				throw new CustomException("Error occurred while processing the image.");
			}

			if ("true".equalsIgnoreCase(imageInfo.get("imageNotFound"))) {
				logger.warn("Image not found in the request. Request: {}", request);
				return image;
			}

			if (imageInfo.get("imageName") != null && imageInfo.get("imagePath") != null) {
				image.setImageName(imageInfo.get("imageName"));
				image.setImagePath(imageInfo.get("imagePath"));
				logger.debug("Image created successfully: {}", image);
			}

		} catch (CustomException ce) {
			logger.error("CustomException occurred: {}", ce.getMessage());
			throw ce;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error processing the image. Request: {}", request, e);
			throw new CustomException("Error processing the image.", e);
		}

		return image;
	}

	private Image createImageFromRequest(Map<String, String> imageInfo, Image image) {
		try {
			logger.debug("Creating image from imageInfo");

			if ("yes".equals(imageInfo.get("serverError"))) {
				logger.error("Error occurred while processing the image. ImageInfo: {}", imageInfo);
				throw new CustomException("Error occurred while processing the image.");
			}

			if ("true".equalsIgnoreCase(imageInfo.get("imageNotFound"))) {
				logger.warn("Image not found in the imageInfo. ImageInfo: {}", imageInfo);
				return null;
			}

			if (imageInfo.get("imageName") != null && imageInfo.get("imagePath") != null) {
				image.setImageName(imageInfo.get("imageName"));
				image.setImagePath(imageInfo.get("imagePath"));
				logger.debug("Image created successfully: {}", image);
			}

		} catch (CustomException ce) {
			logger.error("CustomException occurred: {}", ce.getMessage());
			throw ce;
		} catch (Exception e) {
			logger.error("Error processing the image. ImageInfo: {}", imageInfo, e);
			throw new CustomException("Error processing the image.", e);
		}

		return image;
	}

	private Article createArticleFromRequest(ArticleRequest request, User user, Image image) {
		try {
			logger.debug("Creating article from request");

			Article article = new Article(request.getTitle(), request.getContent(), LocalDateTime.now(), user);

			if (image != null) {
				image.setArticle(article);
				List<Image> images = new ArrayList<>();
				images.add(image);
				article.setImages(images);

				logger.debug("Image associated with the article: {}", image);
			}

			logger.debug("Article created successfully: {}", article);

			return article;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error occurred while creating the article from request", e);
			throw e;
		}
	}

	private boolean isValidArticleRequest(ArticleRequest request) {
		try {
			if (request != null && request.getContent() != null && request.getTitle() != null
					&& request.getUserId() != null) {
				logger.debug("Article request validation successful: {}", request);
				return true;
			} else {
				logger.warn("Invalid article request: {}", request);
				return false;
			}
		} catch (Exception e) {
			logger.error("Error occurred during article request validation", e);
			e.printStackTrace();
			throw e;
		}
	}

	private void updateArticleDetails(Article existingArticle, ArticleRequest updatedArticleRequest) {
		try {
			logger.debug("Updating article details. Existing Article: {}, Updated Request: {}", existingArticle,
					updatedArticleRequest);

			existingArticle.setTitle(updatedArticleRequest.getTitle());
			existingArticle.setContent(updatedArticleRequest.getContent());
			existingArticle.setUpdatedDate(LocalDateTime.now());

			logger.debug("Article details updated successfully. Updated Article: {}", existingArticle);
		} catch (Exception e) {
			logger.error("Error occurred while updating article details", e);
			throw e;
		}
	}

	private ArticleResponse convertArticleToArticleImage(Article article) {
		try {
			logger.debug("Converting Article to ArticleResponse: {}", article);

			String publicationDateFormatted = formatLocalDateTime(article.getPublicationDate());
			String updateDateFormatted = formatLocalDateTime(article.getUpdatedDate());

			ArticleResponse articleResponse = new ArticleResponse(article.getArticleId(), article.getTitle(),
					article.getContent(), article.getUser().getUserId(), publicationDateFormatted, updateDateFormatted,
					extractImagePaths(article.getImages()));

			logger.debug("Conversion successful. ArticleResponse: {}", articleResponse);

			return articleResponse;
		} catch (Exception e) {
			logger.error("Error occurred while converting Article to ArticleResponse", e);
			throw e;
		}
	}

	private Image updateArticleImage(Image existingImage, ArticleRequest updatedArticleRequest) {
		try {
			if (existingImage != null && existingImage.getImageName() != null) {
				boolean deleteResult = imageUpload.deleteImageFromServerByImageName(existingImage.getImageName());

				if (!deleteResult) {
					logger.warn("Failed to delete existing image with name: {}", existingImage.getImageName());
				} else {
					logger.debug("Existing image with name {} deleted successfully", existingImage.getImageName());
				}
			}

			Map<String, String> imageInfo = imageUpload.saveImage(updatedArticleRequest.getImage());

			if ("yes".equals(imageInfo.get("serverError"))) {
				throw new CustomException("Error occurred while processing the image.");
			}

			if ("true".equalsIgnoreCase(imageInfo.get("imageNotFound"))) {
				logger.warn("No image found in the updated request");
				return null;
			}

			Image updatedImage = createImageFromRequest(imageInfo, existingImage);
			logger.debug("Image updated successfully: {}", updatedImage);

			return updatedImage;
		} catch (CustomException ce) {
			logger.error("CustomException occurred: {}", ce.getMessage());
			throw ce;
		} catch (Exception e) {
			logger.error("Error updating the article image", e);
			throw new CustomException("Error updating the article image.", e);
		}
	}

	private String formatLocalDateTime(LocalDateTime dateTime) {
		try {
			logger.debug("Formatting LocalDateTime: {}", dateTime);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDateTime = dateTime != null ? dateTime.format(formatter) : "";

			logger.debug("Formatted DateTime: {}", formattedDateTime);

			return formattedDateTime;
		} catch (Exception e) {
			logger.error("Error occurred while formatting LocalDateTime", e);
			throw e;
		}
	}

	private String formatLocalDateTimeAndMinutes(LocalDateTime dateTime) {
		try {
			logger.debug("Formatting LocalDateTime: {}", dateTime);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
			String formattedDateTime = dateTime != null ? dateTime.format(formatter) : "";

			logger.debug("Formatted DateTime: {}", formattedDateTime);

			return formattedDateTime;
		} catch (Exception e) {
			logger.error("Error occurred while formatting LocalDateTime", e);
			throw e;
		}
	}

}
