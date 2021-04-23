package com.example.demo.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.gson.Gson;

import io.jsonwebtoken.Claims;

public class UsrPrincipal extends UsrVO implements UserDetails{

	/**
	 * add default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	public UsrPrincipal(long usrSn, String usrId, String usrName, Collection<? extends GrantedAuthority> authorities) {
		// TODO Auto-generated constructor stub
		this.setUsrSn(usrSn);
		this.setUsrId(usrId);
		this.setUsrName(usrName);
		this.setAuthorities(authorities);
	}
	
	public static UsrPrincipal buildPayload(Claims info) {
		@SuppressWarnings("unchecked")
		List<String> authorities_string = (List<String>) info.get("authorities");
		List<GrantedAuthority> authorities = new ArrayList<>();
		for(String authority : authorities_string) {
			authorities.add(new SimpleGrantedAuthority(authority));
		}
		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		Map<String,Object> usr = gson.fromJson(info.get("user_name").toString(), Map.class);
		Object usrSnObj = usr.get("usrSn");
		long usrSn = Double.valueOf(String.valueOf(usrSnObj)).longValue();
		String usrName = (String) usr.get("usrName");
		String usrId = (String) usr.get("usrId");
		return new UsrPrincipal(usrSn, usrId, usrName, authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
