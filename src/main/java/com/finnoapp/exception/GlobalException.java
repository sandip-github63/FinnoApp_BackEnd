package com.finnoapp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.finnoapp.payload.response.GenericMessage;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<GenericMessage<?>> handleCustomException(CustomException ex) {
		return ResponseEntity.internalServerError().body(new GenericMessage<>(ex.getMessage(), null, false));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<GenericMessage<?>> handleException(CustomException ex) {
		return ResponseEntity.internalServerError().body(new GenericMessage<>("something went wrong", null, false));
	}

}
