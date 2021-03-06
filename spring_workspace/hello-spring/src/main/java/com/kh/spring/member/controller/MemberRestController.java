package com.kh.spring.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

/**
 * @RestController : 모든 handler가 @ResponseBody 처리된다.
 * (@Controller + @Component)
 * 
 */

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberRestController {
	
	@Autowired
	private MemberService memberService;
	
	/**
	 * 모든 회원을 조회하는 요청
	 * 페이징 처리 필수
	 * cPage = 1(기본값)
	 * numPerPage = 5
	 * 
	 * Mybatis RowBounds 사용할 것
	 * @return
	 */
	@GetMapping("/")
	public List<Member> memberList(@RequestParam(defaultValue ="1") int cPage){
		log.debug("/member 요청!");
		
        int numPerPage = 5;
        Map<String, Object> param = new HashMap<>();
       param.put("numPerPage", numPerPage);
       param.put("cPage", cPage);

       List<Member> list = memberService.selectAll(param);

//     int totalContents = memberService.getTotalMember();
           
       return list;
	}
	
	@GetMapping("/{id}")
//	public ResponseEntity<Member> member(@PathVariable String id) {
	public ResponseEntity<?> member(@PathVariable String id) {
		log.debug("id = {}", id);
		Member member = memberService.selectOneMember(id);
		if(member == null)
			return ResponseEntity.notFound().build();		//404
		
		return ResponseEntity.ok().body(member);			//200
	}
}
