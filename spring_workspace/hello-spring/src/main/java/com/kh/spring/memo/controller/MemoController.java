package com.kh.spring.memo.controller;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.memo.model.service.MemoService;
import com.kh.spring.memo.model.vo.Memo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/memo")
public class MemoController {
	
	@Autowired
	private MemoService memoService;
	
	/**
	 * 
	 * AOP의 실행구조
	 * 
	 * MemoController.memo ------> MemoService.selectMemoList
	 * 
	 * MemoController.memo --- Proxy객체 ---> Target객체(MemoService.selectMemoList)
	 * 
	 * @param model
	 */
	
	
	@GetMapping("/memo.do") //2
	public String memoList(Model model) {
		//proxy 확인
		log.debug("proxy = {}", memoService.getClass());
		
		try {
			List<Memo> list = memoService.selectMemoList();
			log.debug("list = {}", list);
			model.addAttribute("list", list);
		}catch(Exception e){
			//1. 로깅작업
			log.error("메모 조회 오류!", e);
			//2. 다시 spring container에 던질 것.
			throw e;
		}
		
		return "memo/memo";
	}
	
	@PostMapping("/insertMemo.do")
	public String memoEnroll(@ModelAttribute Memo memo, RedirectAttributes redirectAttr) {
		log.debug("memo = {}", memo);
		try {			
			//1. 업무로직
			int result = memoService.insertMemo(memo);
			String msg = "메모 등록 성공!"; 
			//2. 사용자피드백 준비 및 리다이렉트
			redirectAttr.addFlashAttribute("msg",msg);			
		}catch(Exception e) {
			//1. 로깅작업
			log.error(e.getMessage(), e);
			//2. 다시 spring container에 던질 것.
			throw e;
		}
		return "redirect:/memo/memo.do";
	}
}
