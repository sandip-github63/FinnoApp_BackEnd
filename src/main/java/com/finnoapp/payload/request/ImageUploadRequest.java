package com.finnoapp.payload.request;

import org.springframework.web.multipart.MultipartFile;

public class ImageUploadRequest {

	private Long articleId;

	private MultipartFile image;

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public ImageUploadRequest(Long articleId, MultipartFile image) {
		super();
		this.articleId = articleId;
		this.image = image;
	}

	public ImageUploadRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

}
