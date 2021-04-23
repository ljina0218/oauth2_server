package com.example.demo.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class CustomRequestWrapper extends HttpServletRequestWrapper{
	
	private final Map<String,String> customHeader;
	
	public CustomRequestWrapper(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
		this.customHeader = new HashMap<>();
	}
	
	/***********************************
	 * 
	 * response 에는 addHeader가 있지만 request는 ....
	 * HttpServletRequestWrapper를 감싸서 
	 * 		getHeader 메소드를 재정의 하여 수정 !!
	 * 
	 */
	
	public void putHeader(String name, String value) {
		this.customHeader.put(name, value);
	}
	
	@Override
	public Enumeration<String> getHeaders(String name) {
		// TODO Auto-generated method stub
		ArrayList<String> list = Collections.list(super.getHeaders(name));
		if(customHeader.containsKey(name)) {
			list.add(customHeader.get(name));
		}
		return Collections.enumeration(list);
	}
	
	@Override
	public String getHeader(String name) {
		// TODO Auto-generated method stub
		String value = super.getHeader(name);
		if(customHeader.containsKey(name)) {
			value = customHeader.get(name);
		}
		return value;
	}

}
