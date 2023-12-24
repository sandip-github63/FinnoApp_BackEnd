package com.finnoapp.payload.request;

import org.springframework.web.multipart.MultipartFile;

public class ArticleRequest {

	private Long articleId;

	private String title;

	private String content;

	private Long userId;

	private MultipartFile image;

	public ArticleRequest(String title, String content, Long userId, MultipartFile image) {
		super();
		this.title = title;
		this.content = content;
		this.userId = userId;
		this.image = image;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArticleRequest(String title, String content, Long userId) {
		super();
		this.title = title;
		this.content = content;
		this.userId = userId;
	}

	public ArticleRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

}
