package com.example.demo.config;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.model.UsrVO;
import com.example.demo.mvc.service.UsrService;


@Configuration
public class CustomAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired private UsrService usrService;
	@Autowired private PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String authHeader = request.getHeader("Authorization");
		
		String authHeaderEncode = authHeader.split(" ")[1];
		Decoder decoder = Base64.getDecoder();
		byte[] decodedBytes = decoder.decode(authHeaderEncode);
		String clientId = new String(decodedBytes).split(":")[0];
		
		if(AuthServerConfig.client_id.equals(clientId)) {
			String username = authentication.getName();
			String rawpassword = authentication.getCredentials().toString();
			UsrVO usrPVO = new UsrVO();
			usrPVO.setUsrId(username);
			UsrVO usrRVO = usrService.selectUsr(usrPVO);
			if(usrRVO == null) {
				return null;
			}else {
				List<GrantedAuthority> roles = new ArrayList<>();
				roles.add(new SimpleGrantedAuthority("ROLE_USER"));
				String password = usrRVO.getUsrPw();
				if(passwordEncoder.matches(rawpassword, password)) {
					Map<String,Object> principal = buildPrincipal(usrRVO);
					return new UsernamePasswordAuthenticationToken(principal, rawpassword, roles);
				}else {
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true; // false *********
	}
	
	private Map<String,Object> buildPrincipal(UsrVO usrRVO){
		Map<String,Object> principal = new HashMap<>();
		principal.put("usrSn", usrRVO.getUsrSn());
		principal.put("usrId", usrRVO.getUsrId());
		principal.put("usrName", usrRVO.getUsrName());
		return principal;
	}

}
