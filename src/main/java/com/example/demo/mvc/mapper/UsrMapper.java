package com.example.demo.mvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.UsrVO;

@Mapper
public interface UsrMapper {
	
	List<UsrVO> selectUsrs();
	
	UsrVO selectUsr(UsrVO usrVO);
	
	int insertUsr(UsrVO usrVO);
	
}
