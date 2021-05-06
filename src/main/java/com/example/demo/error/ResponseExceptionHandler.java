package com.example.demo.error;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.error.exceptions.AuthorizationException;
import com.example.demo.error.exceptions.BadRequestException;
import com.example.demo.error.exceptions.CustomException;


@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<ExceptionErrorData> forbiddenError(AuthorizationException ex, HttpServletRequest request) throws IOException{
		return new ResponseEntity<>(buildErrorBody(ex), HttpStatus.FORBIDDEN);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ExceptionErrorData> invalidParameter(BadRequestException ex, HttpServletRequest request) throws IOException{
		return new ResponseEntity<>(buildErrorBody(ex), HttpStatus.BAD_REQUEST);
	}
	
	private ExceptionErrorData buildErrorBody(CustomException ex) {
		ExceptionErrorData error = new ExceptionErrorData();
		error.setResult(false);
		error.setMessage(ex.getMessage());
		error.setData(ex.getData());
		return error;
	}

}
