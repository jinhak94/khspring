package com.kh.spring.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MemberDaoImpl implements MemberDao {

	@Autowired
	private SqlSessionTemplate session;

	@Override
	public Member selectOneMember(String id) {
		return session.selectOne("member.selectOneMember", id);
	}
	
}
