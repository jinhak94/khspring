package com.kh.spring.member.model.dao;

import java.util.List;
import java.util.Map;

import com.kh.spring.member.model.vo.Member;
import com.kh.spring.memo.model.vo.Memo;

public interface MemberDao {

	int memberEnroll(Member member);

	Member selectOneMember(String id);

	int updateMember(Member member);

	List<Member> selectAll(Map<String, Object> param);

	int getTotalMember();

}
