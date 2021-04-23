package com.example.demo.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.config.AuthServerConfig;
import com.example.demo.model.UsrPrincipal;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
//@Aspect
public class JwtAuthTokenAspect {
	
	@Autowired JwtTokenComponent jwtTokenComponent;
	
	@Before("execution(* com..controller.*Controller.*(..))")
	public void jwtAuthTokenValidate() throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getResponse();
		log.debug("@@@@@ [JwtAuthTokenAspect] request.getRequestURI() : "+ request.getRequestURI());
		
		String jwt = JwtTokenUtils.getJwtFromHeader(request);
		try {
			if(jwt != null && jwtTokenComponent.validateJwtToken(jwt, response)) {
				Claims body = jwtTokenComponent.getUserIdFromJwtToken(jwt);
				String clientId = (String) body.get("client_id");
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
				log.debug("jwt is not in header Authorization || validation fail .... ");
			}
		} catch (Exception e) {
			JwtTokenUtils.clearAccessTokenCookie(response);
			log.debug("error with setting authentication : "+ e.toString());
		}
	}
	
	private void setAuthentication(HttpServletRequest request, UserDetails userDetils) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetils, null, userDetils.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
