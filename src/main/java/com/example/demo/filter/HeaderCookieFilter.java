package com.example.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.common.CustomRequestWrapper;
import com.example.demo.jwt.JwtTokenUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeaderCookieFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
			throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		log.debug("@@@@@ [HeaderCookieFilter] request.getRequestURI() : "+ req.getRequestURI());
		if(isNoFilteringUri(req.getRequestURI())) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String authHeader = req.getHeader("Authorization");
		if(authHeader == null) {
			String token = JwtTokenUtils.getAccessTokenValue(req);
			if(token == null) {
				log.debug("there is no token in Cookie");
				JwtTokenUtils.clearAccessTokenCookie(res);
				filterChain.doFilter(request, response);
			}else {
				log.debug("put Authorization Header with token in requestWrapper");
				CustomRequestWrapper requestWrapper = new CustomRequestWrapper(req);
				requestWrapper.putHeader("Authorization", "Bearer "+token);
				filterChain.doFilter(requestWrapper, response);
			}
		}else {
			filterChain.doFilter(request, response);
		}
		
	}
	
	private boolean isNoFilteringUri(String requestURI) {
		return requestURI.startsWith("/assets")
				|| requestURI.startsWith("/resources")
				|| requestURI.startsWith("/favicon")
				|| requestURI.startsWith("/webjars")
				|| requestURI.startsWith("/publish")
				|| requestURI.startsWith("/login");
	}

}
