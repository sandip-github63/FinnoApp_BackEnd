package com.finnoapp.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "article")
@Entity
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "article_id")
	private Long articleId;

	@Column(name = "title", length = 10000)
	private String title;

	@Column(name = "content", length = 65535)
	private String content;

	@Column(name = "publication_date")
	private LocalDateTime publicationDate;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Rating> rating;

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Image> images;

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Comment> comment;

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Bookmark> bookmark;

	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public List<Rating> getRating() {
		return rating;
	}

	public void setRating(List<Rating> rating) {
		this.rating = rating;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Bookmark> getBookmark() {
		return bookmark;
	}

	public void setBookmark(List<Bookmark> bookmark) {
		this.bookmark = bookmark;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public String getTitle() {
		return title;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
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

	public LocalDateTime getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(LocalDateTime publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Article(String title, String content, User user) {
		super();
		this.title = title;
		this.content = content;
		this.user = user;
	}

	public Article(String title, String content, LocalDateTime publicationDate, User user) {
		super();
		this.title = title;
		this.content = content;
		this.publicationDate = publicationDate;
		this.user = user;
	}

	public Article() {
		super();
		// TODO Auto-generated constructor stub
	}

}
