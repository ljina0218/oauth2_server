package com.example.demo.error.exceptions;

import java.util.Map;

import lombok.Getter;

public class CustomException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	@Getter private String code;
	@Getter private String message;
	@Getter private Map<String, Object> data;

	protected CustomException() { }

	protected CustomException(String code, String message, Map<String, Object> data) {
		super(message);
		CustomExceptionSub(code, message, data);
	}
	
	protected CustomException(String message, Map<String, Object> data) {
		this("", message, data);
	}
	
	protected CustomException(String message) {
		this("", message, null);
	}

	private void CustomExceptionSub(String code, String message, Map<String, Object> data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

}
