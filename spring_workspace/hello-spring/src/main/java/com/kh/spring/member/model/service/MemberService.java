package com.kh.spring.member.model.service;

import com.kh.spring.member.model.vo.Member;

public interface MemberService {

	int memberEnroll(Member member);

	Member selectOneMember(String id);
	int updateMember(Member member);
}