package com.example.demo.mvc.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.ResponseUtils;
import com.example.demo.jwt.JwtTokenUtils;
import com.example.demo.mvc.service.UsrService;

@RestController
public class UsrRestController {
	
	@Autowired private UsrService usrService;
	
	@RequestMapping("/usrs")
	public Map<String, Object> selectUsrName(HttpServletRequest request, Model model){
		String access_token = JwtTokenUtils.getAccessTokenValue(request);
		if(access_token == null) {
			return ResponseUtils.putFail(null, "권한 없음");
		}else {
			return ResponseUtils.putSuccess(usrService.selectUsrs(), access_token);
		}
		
	}
	
	@RequestMapping("/test")
	public Map<String, Object> test(HttpServletRequest request, Model model){
		return ResponseUtils.putFail(null, "test");
	}

}
