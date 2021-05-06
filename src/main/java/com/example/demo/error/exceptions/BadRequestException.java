package com.example.demo.error.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends CustomException {

	private static final long serialVersionUID = 1L;

	protected BadRequestException() { }

	public BadRequestException(String code, String message, Map<String, Object> data) {
		super(code, message, data);
	}
	
	public BadRequestException(String message, Map<String, Object> data) {
		super(message, data);
	}
	
	public BadRequestException(String message) {
		super(message);
	}
	
}
