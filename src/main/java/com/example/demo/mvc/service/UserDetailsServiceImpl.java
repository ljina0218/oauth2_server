package com.example.demo.mvc.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.UsrVO;
import com.example.demo.mvc.mapper.UsrMapper;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UsrMapper usrMapper;
	
	/****************************************************
	 * 
	 * SecurityConfig 
	 * AuthenticationManagerBuilder.userDetailsService
	 * 
	 * @param userId : oauth/token 으로 요청보낸 username 값
	 * @return userDetails : 이 정보가 load되면서 access_token이 발급 되는 듯...
	 * 
	 */
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UsrVO usrPVO = new UsrVO();
		usrPVO.setUsrId(userId);
		UsrVO usrRVO = usrMapper.selectUsr(usrPVO);
		UserDetails userDetails =  new User(usrRVO.getUsrId(), usrRVO.getUsrPw(), getAuthorities());
		return userDetails;
	}
	
	private List<SimpleGrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}
	
}
