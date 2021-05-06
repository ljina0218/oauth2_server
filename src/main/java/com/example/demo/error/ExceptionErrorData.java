package com.example.demo.error;

import java.util.Map;

import lombok.Data;

@Data
public class ExceptionErrorData {
	
	private boolean result;
	private String message;
	private Map<String, Object> data;

}
