package com.example.demo.jwt;

import java.io.IOException;
import java.security.PublicKey;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.common.KeyHelper;
import com.example.demo.config.AuthServerConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenComponent {
	
	public ModelMap getAuthToken(String usrId, String usrPw, HttpServletResponse response) {
		final String url = "http://localhost:8011/oauth/token"; // spring-security-oauth2 를 쓰게 되면 사용할 수 있게 되는 end-point 
		String clientCredentials = AuthServerConfig.client_id +":"+ AuthServerConfig.client_pw;
		String base64client = new String(Base64.encodeBase64(clientCredentials.getBytes()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "Basic "+ base64client);
		
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("username", usrId);
		params.add("password", usrPw); // encoding 된 값이 아닌 raw password ...
		params.add("grant_type", "password");
		
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String,Object>>(params, headers);
		try {
			RestTemplate template = new RestTemplate();
			ModelMap token = template.postForObject(url, request, ModelMap.class);
			if(token != null) {
				JwtTokenUtils.setAccessTokenCookie(token.get("access_token").toString(), response);
			}else {
				JwtTokenUtils.clearAccessTokenCookie(response);
			}
			return token;
		} catch(HttpClientErrorException | HttpServerErrorException ex) {
			String name = ex.getClass().getSimpleName();
			log.error("GETAUTHTOKEN ERROR [" + name + "] error : " + ex.getMessage() + ", body : " + ex.getResponseBodyAsString());
			ModelMap error = new ModelMap();
			error.addAttribute("error", ex.getMessage());
			return error;
		}
	}
	
	public boolean validateJwtToken(String token, HttpServletResponse response) throws IOException {
		try {
			log.debug("validateJwtToken : "+token);
			PublicKey publicKey = KeyHelper.getPublicKey("key/pubkey.txt");
			Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.error("ExpiredJwtException : "+e.toString());
		} catch (MalformedJwtException e) {
			log.error("MalformedJwtException : "+e.toString());
		} catch (SignatureException e) {
			log.error("SignatureException : "+e.toString());
		} catch (IllegalArgumentException e) {
			log.error("IllegalArgumentException : "+e.toString());
		}
		JwtTokenUtils.clearAccessTokenCookie(response);
		return false;
	}
	
	public Claims getUserIdFromJwtToken(String token) throws IOException {
		PublicKey publicKey = KeyHelper.getPublicKey("key/pubkey.txt");
		return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
	}

}
