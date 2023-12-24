package com.finnoapp.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "user_name", unique = true)
	private String userName;

	@Column(name = "phone")
	private String phone;

	@Column(name = "enable")
	private boolean enable = true;

	@Column(name = "about")
	private String about;

	@Column(name = "password")
	private String password;

	@Column(name = "profile")
	private String profile;

	@Column(name = "otp")
	private String otp;

	@Column(name = "otp_expiration_time")
	private LocalDateTime otpExpirationTime;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<UserRole> uRole = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Article> article;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Rating> rating;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Comment> comment;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Bookmark> bookmark;

	public List<Comment> getComment() {
		return comment;
	}

	public List<Bookmark> getBookmark() {
		return bookmark;
	}

	public void setBookmark(List<Bookmark> bookmark) {
		this.bookmark = bookmark;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

	public List<Rating> getRating() {
		return rating;
	}

	public void setRating(List<Rating> rating) {
		this.rating = rating;
	}

	public List<Article> getArticle() {
		return article;
	}

	public void setArticle(List<Article> article) {
		this.article = article;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isEnable() {
		return enable;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public User(Long userId, String firstName, String lastName, String email, String userName, String phone,
			boolean enable, String about, String password, String profile, Set<UserRole> uRole) {
		super();
		System.out.println("inside user parametrized constructor...");
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userName = userName;
		this.phone = phone;
		this.enable = enable;
		this.about = about;
		this.password = password;
		this.profile = profile;
		this.uRole = uRole;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public Set<UserRole> getuRole() {
		return uRole;
	}

	public void setuRole(Set<UserRole> uRole) {
		this.uRole = uRole;
	}

	public User() {
		super();
		System.out.println("inside user default construction....");
	}

	public User(String firstName, String lastName, String email, String userName, String phone, boolean enable,
			String about, String password, String profile) {
		super();
		System.out.println("inside user parametrized constructor...");
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userName = userName;
		this.phone = phone;
		this.enable = enable;
		this.about = about;
		this.password = password;
		this.profile = profile;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public LocalDateTime getOtpExpirationTime() {
		return otpExpirationTime;
	}

	public void setOtpExpirationTime(LocalDateTime otpExpirationTime) {
		this.otpExpirationTime = otpExpirationTime;
	}

}
