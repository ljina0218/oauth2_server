package com.example.demo.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class UsrVO {
	
	private long usrSn;
	private String usrId;
	private String usrPw;
	private String usrName;
	private Collection <? extends GrantedAuthority> authorities;
	
}
