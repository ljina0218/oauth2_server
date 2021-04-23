package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@EnableAuthorizationServer
@Configuration
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter{
	
	public static final String client_id = "client_id";
	public static final String client_pw = "client_pw";
	
	@Autowired private TokenStore tokenStore; // JWTTokenStore
	@Autowired private AuthenticationManager authenticationManager; 
	@Autowired private PasswordEncoder passwordEncoder; 
	@Autowired private UserDetailsService userDetailsService;
	@Autowired private JwtAccessTokenConverter jwtAccessTokenConverter;
	
	// Client에 대한 정보 설정 .... [1] inMemory 방식
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			.withClient(client_id)
			.secret(passwordEncoder.encode(client_pw))
			.authorizedGrantTypes("password", "refresh_token", "authorization_code")
			.scopes("read","write")
			.accessTokenValiditySeconds(60*60)
			.refreshTokenValiditySeconds(6*60*60)
			.autoApprove(true);
	}
	
	// Oauth2 서버가 작동하기 위한 end-point에 대한 정보 설정
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore)
			.authenticationManager(authenticationManager)
			.userDetailsService(userDetailsService)
			.accessTokenConverter(jwtAccessTokenConverter);
	}
	
	// Oauth2 인증서버 자체의 보안 정보를 설정
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		super.configure(security);
	}
	
}
