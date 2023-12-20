package com.finnoapp.payload.request;

public class ArticleRequest {

	private String title;

	private String content;

	private Long userId;

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

}
