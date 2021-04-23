package com.example.demo.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class CookieUtils {
	
	public static void createCookie(String name, String value, int maxAge, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public static void clearCookie(String name, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, null);
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public static String getValue(String name, HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, name);
		String value = cookie != null ? cookie.getValue() : null;
		return value; 
	}

}
