package com.finnoapp.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.finnoapp.payload.response.GenericMessage;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<GenericMessage<?>> handleCustomException(CustomException ex) {
		return ResponseEntity.internalServerError().body(new GenericMessage<>("error", ex.getMessage(), null, false));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<GenericMessage<?>> handleException(CustomException ex) {
		return ResponseEntity.internalServerError()
				.body(new GenericMessage<>("error", "something went wrong", null, false));
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Object> handleMaxSizeException(MaxUploadSizeExceededException exc,
			HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.body(new GenericMessage<>("error", "File size exceeds the allowed limit.", false));
	}

}
