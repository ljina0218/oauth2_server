package com.example.demo.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.UsrVO;
import com.example.demo.mvc.mapper.UsrMapper;

@Service
public class UsrService {
	
	@Autowired private UsrMapper usrMapper;
	
	public List<UsrVO> selectUsrs(){
		return usrMapper.selectUsrs();
	}
	
	public UsrVO selectUsr(UsrVO usrVO) {
		return usrMapper.selectUsr(usrVO);
	}
	
	public int insertUsr(UsrVO usrVO) {
		return usrMapper.insertUsr(usrVO);
	}
	

}
