package com.example.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.config.AuthServerConfig;
import com.example.demo.jwt.JwtTokenComponent;
import com.example.demo.jwt.JwtTokenUtils;
import com.example.demo.model.UsrPrincipal;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthTokenFilter extends OncePerRequestFilter{
	
	@Autowired JwtTokenComponent jwtTokenComponent;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.debug("##### [JwtAuthTokenFilter] request.getRequestURI() : "+ request.getRequestURI());
		if(isNoFilteringUri(request.getRequestURI())) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String jwt = JwtTokenUtils.getJwtFromHeader(request);
		try {
			if(jwt != null && jwtTokenComponent.validateJwtToken(jwt, response)) {
				Claims body = jwtTokenComponent.getUserIdFromJwtToken(jwt);
				String clientId = (String) body.get("client_jd");
				if(AuthServerConfig.client_id.equals(clientId)) {
					UserDetails userDetails = UsrPrincipal.buildPayload(body);
					setAuthentication(request, userDetails);
					log.debug("setAuthentication success : "+ SecurityContextHolder.getContext().getAuthentication());
				}else {
					log.debug("invalid client_id : "+clientId);
					JwtTokenUtils.clearAccessTokenCookie(response);
				}
			}else {
				JwtTokenUtils.clearAccessTokenCookie(response);
				log.debug("jwt is not in header Authorization");
			}
		} catch (Exception e) {
			JwtTokenUtils.clearAccessTokenCookie(response);
			log.debug("error with setting authentication : "+ e.toString());
		}
		filterChain.doFilter(request, response);
	}
	
	private boolean isNoFilteringUri(String requestURI) {
		return requestURI.startsWith("/assets")
				|| requestURI.startsWith("/resources")
				|| requestURI.startsWith("/favicon")
				|| requestURI.startsWith("/webjars")
				|| requestURI.startsWith("/publish")
				|| requestURI.startsWith("/login");
	}
	
	private void setAuthentication(HttpServletRequest request, UserDetails userDetils) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetils, null, userDetils.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
