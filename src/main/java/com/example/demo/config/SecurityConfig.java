package com.example.demo.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsUtils;

import com.example.demo.filter.HeaderCookieFilter;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.authentication.AuthenticationManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
	public HeaderCookieFilter headerCookieFilter(){
		return new HeaderCookieFilter();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if(log.isDebugEnabled()) log.debug("configure(HttpSecurity http) start ~~");
		
		http
			.cors().and() 
			.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll().and()
			.csrf().requireCsrfProtectionMatcher(new CsrMatcher()).and() // ?????? matcher??? csrf ?????? ??????
			.formLogin().disable() // ????????? ??????????????? ?????? ?????? false
			.httpBasic().disable(); // http ?????? ???????????? ?????? ?????? false
		
		http.addFilterBefore(headerCookieFilter(), UsernamePasswordAuthenticationFilter.class);
		
		http.exceptionHandling().authenticationEntryPoint((request, response, e)->{
        	response.setContentType("application/json;charset=UTF-8");
        	response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        	Map<String, Object> data = new HashMap<>();
			data.put("code", "FORBIDDEN");
        	try {
				response.getWriter().write(
					new JSONObject()
							.put("result", false)
							.put("message", "????????? ???????????? ????????? ???????????? ????????????.")
							.put("data", data).toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
        });
		
		if(log.isDebugEnabled()) log.debug("configure(HttpSecurity http) done !!");
	}
	
	class CsrMatcher implements RequestMatcher{
		@Bean
		public List<AntPathRequestMatcher> csrfPathMatcher(){
			List<AntPathRequestMatcher> matchers = new ArrayList<>();
			matchers.add(new AntPathRequestMatcher("/payment"));
			return matchers;
		}
		
		@Override
		public boolean matches(HttpServletRequest request) {
			for(AntPathRequestMatcher matcher: csrfPathMatcher()) {
				if(matcher.matches(request)) return true;
			}
			return false;
		}
	}
	
	/**********************************************
	 * oauth2 ????????? ?????? ?????? ...
	 */
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
//	@Bean 
//	public UserDetailsService userDetailsService() {
//		return new UsrService();
//	}
	
	@Autowired private CustomAuthenticationProvider authProvider;
	
	/**
	 * 
	 * 	/oauth2/token ?????? ??? 
	 *		?????? ????????? ????????? authentication ????????? ???????????? ??? ...
	 *
	 *	(1) UserDetailService - loadByUserName(..)
	 *	(2) AuthenticationProvider - authenticate
	 *
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(userDetailsService()).passwordEncoder(encoder()); // (1)
		auth.authenticationProvider(authProvider); // (2)
	}

}
