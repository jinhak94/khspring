package com.kh.spring.member.controller;

import java.beans.PropertyEditor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.google.gson.Gson;
import com.kh.spring.member.model.exception.MemberException;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

/**
 * Model
 * - view단에서 처리할 데이터 저장소. Map객체
 * 1. Model<<interface>>
 * 		- viewName 리턴
 * 		- addAttribute(k, v)
 * 2. ModelMap
 * 		- viewName 리턴
 * 		- addAttribute(k, v)
 * 3. ModelAndView 
 * 		- viewName(jsp위치, redirect Location) 포함, ModelAndView 객체 리턴
 * 		- addObject(k, v)
 * 		- RedirectAttributes 객체와 함께 사용하지 말 것.
 * 
 * @ModelAttribute
 * 1. 메소드 레벨로 사용
 * 		- 해당 메소드의 리턴값을 모델에 저장해서 모든 요청에 사용
 * 
 * 2. 메소드 매개변수에 지정
 * 		- model에 저장된 동일한 이름의 속성(보통 session에서 찾음)이 있는 경우 getter로 사용 가능
 * 		- 해당 매개변수를 model 속성으로 저장
 * 			- 커맨드객체에 @ModelAttribute(속성명)으로 지정
 * 			- 단순 사용자 입력값은 @RequestParam으로 처리
 * 
 */

@Slf4j
@Controller
@RequestMapping("/member") //1
//2개 이상의 경우 아래와 같이 String 배열로 등록
//@SessionAttributes(value = {"loginMember", "anotherValue"})
@SessionAttributes("loginMember")
public class MemberController {
	
	// private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@ModelAttribute("common")
	public Map <String, Object> common(){
		log.debug("@ModelAttribute - common 실행!");
		Map<String, Object> map = new HashMap<>();
		map.put("adminEmail", "admin@spring.kh.com");
		
		return map;
	}
	
	@GetMapping("/memberEnroll.do") //2
	public void memberEnroll() {
		//1번과 2번을 합쳐서 /member/memberEnroll.do로 찾음
		//ViewTranslator에 의해서 
		//요청 url에서 view단 jsp주소를 추론한다.
		
		//return "member/memberEnroll";
	}
	
	@PostMapping("/memberEnroll.do")
	public String memberEnroll(Member member, RedirectAttributes redirectAttr) {
		log.info("member = {}", member);
		try {
			//0. 암호화처리
			String rawPassword = member.getPassword();
			String encodedPassword = bcryptPasswordEncoder.encode(rawPassword);
			log.info("rawPassword = {}", rawPassword);
			log.info("encodedPassword = {}", encodedPassword);			
			member.setPassword(encodedPassword);
			
			//1. 업무로직
			int result = memberService.memberEnroll(member);
			String msg = result > 0 ? "회원가입 성공!" : "회원가입 실패!"; 
			//2. 사용자피드백 준비 및 리다이렉트
			redirectAttr.addFlashAttribute("msg",msg);			
		}catch(Exception e) {
			//1. 로깅작업
			log.error(e.getMessage(), e);
			//2. 다시 spring container에 던질 것.
			throw e;
		}
		return "redirect:/";
	}
	
	/**
	 * 커맨드객체 이용시 사용자입력값을 
	 * 특정 필드 타입으로 변환할 editor 객체를 설정
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//Member.birthday:java.sql.Date 타입 처리
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//커스텀에디터 생성
		//true는 빈 문자열이 와도 괜찮다는 의미. 오류나지 않고 null로 변환
		PropertyEditor editor = new CustomDateEditor(sdf, true);
		//왼쪽거 변환하려면 오른쪽 editor를 쓰기
		binder.registerCustomEditor(java.sql.Date.class, editor);
	}
	
	@GetMapping("/login.do")
	public ModelAndView login(ModelAndView mav) {
//			@RequestParam String next) {
		mav.addObject("test", "hello world");
		mav.setViewName("member/login");
		
		return mav;
	}
	
	@PostMapping("/login.do")
	public ModelAndView login(
			@RequestParam String id, 
			@RequestParam String password,
			ModelAndView mav,
			HttpServletRequest request) {
		FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
		try{
			log.info("id = {}, password = {}", id, password);
			//1. 업무로직 : 해당 id의 member 조회
			Member member = memberService.selectOneMember(id);
			log.info("member - {}", member);
			log.info("encodedPassword = {}", bcryptPasswordEncoder.encode(password));
			
			//2. 로그인 여부처리
			if(member != null && bcryptPasswordEncoder.matches(password, member.getPassword())) {
				//로그인 성공
				//기본적으로 request scope 속성에 저장되므로
				//jsp에서 request.getAttribute로 꺼내와야 함.
				//우리가 원하는건 session 속성에 저장되는 것을 원함.
				//클래스레벨에 @SessionAttributes("loginMember") 지정하면
				//session scope에 저장
				//spring container는 loginMember로 된 이름을 session scope에 저장
				mav.addObject("loginMember", member);
				flashMap.put("msg","로그인에 성공하셨습니다.");
			}else {
				//로그인 실패
				flashMap.put("msg","아이디 또는 비밀번호가 일치하지 않습니다.");
			}
			//3. 사용자피드백 및 리다이렉트
		}catch(Exception e) {
			//1. logging
			log.error(e.getMessage(), e);
			//2. spring container에게 예외를 다시 던져서
			//	 error 페이지로 이동시킨다.
			throw e;
		}
		mav.setViewName("redirect:/");
		return mav;
	}
	
	/**
	 * @SessionAttributes를 통한 로그인은
	 * SessionStatus 객체를 통해서 사용완료 처리함으로 로그아웃한다.
	 * 
	 * @return
	 */
	@GetMapping("/logout.do")
	public String logout(SessionStatus sessionStatus){
		if(!sessionStatus.isComplete()) {
			sessionStatus.setComplete();
		}
		return "redirect:/";
	}
		
	@GetMapping("/memberDetail.do")
	public ModelAndView memberDetail(@ModelAttribute("loginMember") Member loginMember, ModelAndView mav) {
		log.info("loginMember = {}", loginMember);
		mav.setViewName("member/memberDetail");
		return mav;
	}
	
	@PostMapping("/memberUpdate.do")
	public String memberUpdate(@ModelAttribute Member member, ModelMap model, RedirectAttributes redirectAttributes){
		try {
			log.debug("member = {} ", member);
				
			//1.비지니스로직 실행
			int result = memberService.updateMember(member);
			
			//2.처리결과에 따라 view단 분기처리
			String msg = "회원정보수정성공!";
			if(result > 0){
				//회원정보 수정 성공시 session객체 갱신
				Member updateMember = memberService.selectOneMember(member.getId());
				model.addAttribute("loginMember", updateMember);
			}
			else {
				throw new MemberException("해당회원이 존재하지 않습니다 : " + member.getId());
			}
			redirectAttributes.addFlashAttribute("msg", msg);
			
		} catch(Exception e) {
			log.error("회원 정보 수정 실패", e);
			throw e;
		}
		return "redirect:/";
	}
	
	
	/**
	 * Spring Ajax
	 * 1. BeanNameViewResolver : jsonView라는 bean을 이용해서 json 출력
	 * 2. 응답스트림 직접 작성 : 응답출력스트림에 json 문자열 출력
	 * 3. @ResponseBody : handler의 리턴객체를 messageConverter빈에 의해 json 변환 출력
	 * 4. ResponseEntity
	 * 
	 * @param id
	 * @return
	 */
	
	@GetMapping("/checkIdDuplicate1.do")
	public String checkIdDuplicate1(@RequestParam String id, Model model) {
		log.debug("id = {}",id);
		//1. 업무로직
		Member member = memberService.selectOneMember(id);
		//2. model 속성 지정(jsonView에 의해 json 문자열로 변환 후, 응답출력)
		boolean usable = member == null;
		model.addAttribute("usable", usable);
		model.addAttribute("name", "홍길동");		
		model.addAttribute("num", 123);		
		
		return "jsonView";
	}
	
	@GetMapping(value="/checkIdDuplicate2.do"
			, produces=MediaType.APPLICATION_JSON_VALUE)
	public void checkIdDuplicate2(@RequestParam String id, 
			HttpServletResponse response, 
			PrintWriter out) {
		//response.getWriter().print("hello")
		//1. 업무로직
		Member member = memberService.selectOneMember(id);
		boolean usable = (member == null);
		Map<String, Object> map = new HashMap<>();
		map.put("usable", usable);
		map.put("name", "신사임당");
		map.put("serverTime", new Date());
		
		//2. json 문자열 응답메세지 출력
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		new Gson().toJson(map, out);
	}
	
	/**
	 * RequestMappingHandlerAdapter빈에 등록된
	 * MessageConverter빈에 의해 json 변환 처리
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/checkIdDuplicate3.do")
	@ResponseBody //우리가 java로 처리한 응답을 JSON으로 변환해서 응답메세지에 직접 써줌
	public Map<String, Object> checkIdDuplicate3(@RequestParam String id){
		Member member = memberService.selectOneMember(id);
		boolean usable = (member == null);

		Map<String, Object> map = new HashMap<>();
		map.put("usable", usable);
		map.put("name", "신사임당");
		map.put("serverTime", System.currentTimeMillis()); //new Date().getTime()하고 같음
		
		
		return map;
	}
	
	/**
	 * 1. status code
	 * 2. header값을 자유롭게 설정
	 * 3. @ResponseBody 리턴객체를 json변환후 응답메세지에 작성
	 * @return
	 */
	@GetMapping("/checkIdDuplicate4.do")
//	public ResponseEntity<Map<String, Object>> checkIdDuplicate4(@RequestParam String id){
	public ResponseEntity<?> checkIdDuplicate4(@RequestParam String id){
		//업무 로직
		Member member = memberService.selectOneMember(id);
		boolean usable = (member == null);

		//2. json 변환 객체
		Map<String, Object> map = new HashMap<>();
		map.put("usable", usable);
		map.put("name", "이순신");
		map.put("serverTime", System.currentTimeMillis()); //new Date().getTime()하고 같음
		
//		return ResponseEntity
//				.ok()
//				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
//				.body(map);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<>(map, headers, HttpStatus.OK);
	}
}












