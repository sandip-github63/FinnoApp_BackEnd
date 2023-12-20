package com.finnoapp.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finnoapp.exception.CustomException;
import com.finnoapp.model.Article;
import com.finnoapp.model.User;
import com.finnoapp.payload.request.ArticleRequest;
import com.finnoapp.payload.response.ArticleResponse;
import com.finnoapp.payload.response.GenericMessage;
import com.finnoapp.service.ArticleService;
import com.finnoapp.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/article")
public class ArticleController {

	@Autowired
	ArticleService articleService;

	@Autowired
	UserService userService;

	@PostMapping("/create")
	public ResponseEntity<?> createArticle(@RequestBody ArticleRequest request) {
		try {
			if (isValidArticleRequest(request)) {
				Long userId = request.getUserId();
				User user = userService.getUserById(userId);

				if (user != null) {

					Article article = this.articleService.addArticle(
							new Article(request.getTitle(), request.getContent(), LocalDateTime.now(), user));

					if (article != null) {
						return ResponseEntity.status(HttpStatus.OK)
								.body(new GenericMessage<Void>("Article added..", true));
					} else {
						throw new CustomException("Article not added..");
					}
				} else {
					throw new CustomException("User Id is not exists in our database");
				}
			}
		} catch (CustomException ce) {
			throw ce;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			throw e;
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericMessage<>("invalid request", false));
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
				// You may want to create a DTO (Data Transfer Object) for the response
				// to include only necessary information and not expose internal details.

				// formatted the date and time
				String publicationDateFormatted = this.formatLocalDateTime(article.getPublicationDate());
				String updateDateFormatted = this.formatLocalDateTime(article.getUpdatedDate());

				ArticleResponse articleResponse = new ArticleResponse(article.getArticleId(), article.getTitle(),
						article.getContent(), article.getUser().getUserId(), publicationDateFormatted,
						updateDateFormatted);

				return ResponseEntity.status(HttpStatus.OK)
						.body(new GenericMessage<>("article fetched successfully..", articleResponse, true));
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

	@GetMapping("/all-articles")
	public ResponseEntity<?> getListArticle() {
		try {
			List<Article> allArticles = articleService.getAllArticles();

			if (!allArticles.isEmpty()) {
				List<ArticleResponse> list = allArticles.stream().map(article -> {

					String publicationDateFormatted = this.formatLocalDateTime(article.getPublicationDate());
					String updateDateFormatted = this.formatLocalDateTime(article.getUpdatedDate());

					return new ArticleResponse(article.getArticleId(), article.getTitle(), article.getContent(),
							article.getUser().getUserId(), publicationDateFormatted, updateDateFormatted);

				}

				).collect(Collectors.toList());

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

	@DeleteMapping("/{articleId}")
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
