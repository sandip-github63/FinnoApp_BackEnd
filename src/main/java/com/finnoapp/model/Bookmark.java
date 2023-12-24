package com.finnoapp.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "book_mark")
@Entity
public class Bookmark {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "book_mark_id")
	private Long bookMarkId;

	@Column(name = "creation_date")
	private LocalDateTime creationDate;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne
	@JoinColumn(name = "articleId")
	private Article article;

	public Long getBookMarkId() {
		return bookMarkId;
	}

	public void setBookMarkId(Long bookMarkId) {
		this.bookMarkId = bookMarkId;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Bookmark(Long bookMarkId, LocalDateTime creationDate, User user, Article article) {
		super();
		this.bookMarkId = bookMarkId;
		this.creationDate = creationDate;
		this.user = user;
		this.article = article;
	}

	public Bookmark() {
		super();
		// TODO Auto-generated constructor stub
	}

}
