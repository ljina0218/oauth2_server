package com.example.demo.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.core.io.ClassPathResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeyHelper {
	
	public static PublicKey getPublicKey(String pubKeyFilePath) throws IOException {
		BufferedReader brPublicKey = null;
		String sPublicKey="";
		
		ClassPathResource classPathResource = new ClassPathResource(pubKeyFilePath);
		if(classPathResource.exists() == false){
		    log.error("Invalid filePath : {}", pubKeyFilePath);
		    throw new IllegalArgumentException();
		}
		InputStreamReader isReader = new InputStreamReader(classPathResource.getInputStream(), "UTF-8");
		try {
			brPublicKey = new BufferedReader(isReader);
			sPublicKey = brPublicKey.readLine();   // First Line Read
			log.info("Pubilc Key Read");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try{
				if (brPublicKey != null)
					brPublicKey.close();
			}catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		byte[] bPublicKey = Base64.getDecoder().decode(sPublicKey.getBytes());
		PublicKey  publicKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bPublicKey);
			publicKey = keyFactory.generatePublic(publicKeySpec);
		  
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return publicKey;
	}
	
	public static PrivateKey getPrivateKey(String privKeyFilePath) throws IOException {
		BufferedReader brPrivateKey = null;
		String sPrivateKey="";
		
		ClassPathResource classPathResource = new ClassPathResource(privKeyFilePath);
		if(classPathResource.exists() == false){
		    log.error("Invalid filePath : {}", privKeyFilePath);
		    throw new IllegalArgumentException();
		}
		InputStreamReader isReader = new InputStreamReader(classPathResource.getInputStream(), "UTF-8");
		try {
			brPrivateKey = new BufferedReader(isReader);
			sPrivateKey = brPrivateKey.readLine(); // First Line Read
			log.info("Read Private key");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (brPrivateKey != null)
					brPrivateKey.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		byte[] bPrivateKey = Base64.getDecoder().decode(sPrivateKey.getBytes());
		PrivateKey privateKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bPrivateKey);
			privateKey = keyFactory.generatePrivate(privateKeySpec);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return privateKey;
	}
	
}
