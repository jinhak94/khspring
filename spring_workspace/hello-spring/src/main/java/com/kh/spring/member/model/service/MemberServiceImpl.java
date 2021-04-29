package com.kh.spring.member.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;

	@Override
	public int memberEnroll(Member member) {
		return memberDao.memberEnroll(member);
	}

	@Override
	public Member selectOneMember(String id) {
		return memberDao.selectOneMember(id);
	}
	
	@Override
	public int updateMember(Member member) {
		return memberDao.updateMember(member);
	}

	@Override
	public List<Member> selectAll(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return memberDao.selectAll(param);
	}

	@Override
	public int getTotalMember() {
		return memberDao.getTotalMember();
	}
}
