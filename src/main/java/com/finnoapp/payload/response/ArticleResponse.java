package com.finnoapp.payload.response;

public class ArticleResponse {

	private Long articleId;

	private String title;

	private String content;

	private Long userId;

	private String publicationDate;

	private String updatedDate;

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getTitle() {
		return title;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public ArticleResponse(Long articleId, String title, String content, Long userId) {
		super();
		this.articleId = articleId;
		this.title = title;
		this.content = content;
		this.userId = userId;
	}

	public ArticleResponse(Long articleId, String title, String content, Long userId, String publicationDate,
			String updatedDate) {
		super();
		this.articleId = articleId;
		this.title = title;
		this.content = content;
		this.userId = userId;
		this.publicationDate = publicationDate;
		this.updatedDate = updatedDate;
	}

	public ArticleResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}
