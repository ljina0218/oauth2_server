package com.example.demo.jwt;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.example.demo.common.KeyHelper;

import lombok.extern.slf4j.Slf4j;

@Configuration
public class JwtTokenConfig {
	
	public static final String signKey = "jwtsignkey";
	
	@Bean 
	public TokenStore tokenStore() throws IOException {
		//return new InMemoryTokenStore(); 
		return new JwtTokenStore(jwtAccessTokenConverter());
	}
	
	/**********************************************
	 * 
	 * access_token은 bearer 형식 : 단순히 암호화된 문자열 => 인증서버에서 회원확인을 위한 유효성 체크 필요
	 * jwt : json string이 암호화된 문자열, 특정한 정보 세팅 가능 => 회원확인을 위해 인증서버 거칠 필요 업
	 * @return
	 * @throws IOException 
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() throws IOException {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//		converter.setSigningKey(signKey);
		PublicKey publicKey = KeyHelper.getPublicKey("key/pubkey.txt");
		PrivateKey privateKey = KeyHelper.getPrivateKey("key/prvkey.txt");
		KeyPair keypair = new KeyPair(publicKey, privateKey);
		converter.setKeyPair(keypair);
		return converter;
	}
	
	

}
