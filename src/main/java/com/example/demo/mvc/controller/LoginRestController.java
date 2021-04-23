package com.example.demo.mvc.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.ResponseUtils;
import com.example.demo.jwt.JwtTokenComponent;
import com.example.demo.model.UsrVO;
import com.example.demo.mvc.service.UsrService;

@RestController
@RequestMapping("/login")
public class LoginRestController {
	
	@Autowired private UsrService usrService;
	@Autowired private JwtTokenComponent jwtTokenComponent;

	@PostMapping("/auth")
	public Map<String,Object> selectUsr(HttpServletRequest request, HttpServletResponse response, @ModelAttribute UsrVO usrPVO, Model model) {
		UsrVO usrRVO = usrService.selectUsr(usrPVO);
		if(usrRVO == null) return ResponseUtils.putFail(null, "로그인 오류");
		Map<String,Object> token = jwtTokenComponent.getAuthToken(usrPVO.getUsrId(), usrPVO.getUsrPw(), response);
		if(token != null) {
			if(token.get("error") == null) return ResponseUtils.putSuccess(token, null);
			else return ResponseUtils.putFail(token, "토큰 발급 오류");
		}else {
			return ResponseUtils.putFail(null, "토큰 발급 오류");
		}
	}
	
	@GetMapping("/auth2")
	public Map<String,Object> selectUsr2(HttpServletRequest request, HttpServletResponse response,  Model model) {
		String usrId = request.getParameter("usrId");
		String usrPw = request.getParameter("usrPw");
		UsrVO usrPVO = new UsrVO();
		usrPVO.setUsrId(usrId);
		usrPVO.setUsrPw(usrPw);
		UsrVO usrRVO = usrService.selectUsr(usrPVO);
		if(usrRVO == null) return ResponseUtils.putFail(null, "로그인 오류");
		Map<String,Object> token = jwtTokenComponent.getAuthToken(usrPVO.getUsrId(), usrPVO.getUsrPw(), response);
		if(token != null) {
			if(token.get("error") == null) return ResponseUtils.putSuccess(token, null);
			else return ResponseUtils.putFail(token, "토큰 발급 오류");
		}else {
			return ResponseUtils.putFail(null, "토큰 발급 오류");
		}
	}

}
