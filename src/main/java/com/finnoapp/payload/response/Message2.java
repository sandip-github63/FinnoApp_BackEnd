package com.finnoapp.payload.response;

public class Message2 {

	private String message;
	private String status;
	private UserWithAuthoritiesDTO userWithAuthorities;

	public Message2(String message, String status, UserWithAuthoritiesDTO userWithAuthorities) {
		this.message = message;
		this.status = status;
		this.userWithAuthorities = userWithAuthorities;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserWithAuthoritiesDTO getUserWithAuthorities() {
		return userWithAuthorities;
	}

	public void setUserWithAuthorities(UserWithAuthoritiesDTO userWithAuthorities) {
		this.userWithAuthorities = userWithAuthorities;
	}

}
