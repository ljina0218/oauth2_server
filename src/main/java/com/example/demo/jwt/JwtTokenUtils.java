package com.example.demo.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.common.CookieUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenUtils {
	
	private static final String access_token = "access_token";
	
	public static String getJwtFromHeader(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.replace("Bearer ", "");
		}
		log.debug("authHeader is null");
		return null;
	}
	
	public static void setAccessTokenCookie(String token, HttpServletResponse response) {
		CookieUtils.createCookie(access_token, token, 60*60, response);
	}
	
	public static void clearAccessTokenCookie(HttpServletResponse response) {
		CookieUtils.clearCookie(access_token, response);
	}
	
	public static String getAccessTokenValue(HttpServletRequest request) {
		return CookieUtils.getValue(access_token, request);
	}

}
