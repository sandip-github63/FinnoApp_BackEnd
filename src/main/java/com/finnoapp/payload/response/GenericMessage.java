package com.finnoapp.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GenericMessage<T> {

	private String message;

	private T data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public GenericMessage(String message, T data) {
		super();
		this.message = message;
		this.data = data;
	}

	public GenericMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

}
