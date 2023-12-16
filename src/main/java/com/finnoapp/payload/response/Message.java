package com.finnoapp.payload.response;

import com.finnoapp.model.User;

public class Message {

	private String message;

	private String status;

	private User user = null;

	public Message(String message, String status, User user) {
		this.message = message;
		this.status = status;
		this.user = user;
	}

	public Message(String message, String status) {
		this.message = message;
		this.status = status;

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Message() {

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
