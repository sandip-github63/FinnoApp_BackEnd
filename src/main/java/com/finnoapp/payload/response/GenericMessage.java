package com.finnoapp.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GenericMessage<T> {

	private String message;

	private T data;

	private boolean hasData = false;

	public boolean isHasData() {
		return hasData;
	}

	public void setHasData(boolean hasData) {
		this.hasData = hasData;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return this.data;

	}

	public void setData(T data) {
		this.data = data;
	}

	public GenericMessage(String message, T data, boolean hasData) {
		super();
		this.message = message;
		this.data = data;
		this.hasData = hasData;

	}

	public GenericMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GenericMessage(String message, boolean hasData) {
		super();
		this.message = message;
		this.hasData = hasData;
	}

}
