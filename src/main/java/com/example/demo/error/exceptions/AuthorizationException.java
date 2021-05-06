package com.example.demo.error.exceptions;

import java.util.Map;

public class AuthorizationException extends CustomException {

	private static final long serialVersionUID = 1L;

	protected AuthorizationException() { }

	public AuthorizationException(String code, String message, Map<String, Object> data) {
		super(code, message, data);
	}
	
	public AuthorizationException(String message, Map<String, Object> data) {
		super(message, data);
	}
	
	public AuthorizationException(String message) {
		super(message);
	}
	
}
