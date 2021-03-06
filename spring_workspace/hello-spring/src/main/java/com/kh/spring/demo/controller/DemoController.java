package com.kh.spring.demo.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.demo.model.service.DemoService;
import com.kh.spring.demo.model.vo.Dev;

import lombok.extern.slf4j.Slf4j;

/**
* HttpServletRequest
* HttpServletResponse
* HttpSession
* java.util.Locale : 요청에 대한 Locale
* InputStream/Reader : 요청에 대한 입력스트림
* OutputStream/Writer : 응답에 대한 출력스트림. ServletOutputStream, PrintWriter
컨트롤러클래스 메소드가 가질수 있는 파라미터
* @PathVariable: 요청url중 일부를 매개변수로 취할 수 있다.
* @RequestParam
* @RequestHeader
* @CookieValue
* @RequestBody
뷰에 전달할 모델 데이터 설정
* ModelAndView
* Model
* ModelMap 
* @ModelAttribute : model속성에 대한 getter
* @SessionAttribute : session속성에 대한 getter
* SessionStatus: @SessionAttribute로 등록된 속성에 대하여 사용완료(complete)처리
* Command객체 : http요청 파라미터를 커맨드객체에 저장한 VO객체
* Error, BindingResult : Command객체에 저장결과, Command객체 바로 다음위치시킬것.
기타
* MultipartFile : 업로드파일 처리 인터페이스. CommonsMultipartFile
* RedirectAttributes : DML처리후 요청주소 변경을 위한 redirect를 지원
 *
 */

@Slf4j
@Controller
@RequestMapping("/demo") //클래스 레벨로 url mapping
public class DemoController {

	//@Slf4j에 의해 생성되는 코드
	//Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DemoService demoService; // = new DemoServiceImpl();

	/**
	 * 사용자 요청 url에 따라 호출되는 메소드
	 * @return
	 */
//	@RequestMapping("/demo/devForm.do")
	@RequestMapping("/devForm.do")
	public String devForm() {
		return "demo/devForm";
	}
	
//	@RequestMapping("/demo/dev1.do") //GET POST 처리
	@RequestMapping(value="/dev1.do", method=RequestMethod.POST) //POST만 처리
	public String dev1(HttpServletRequest request, HttpServletResponse response, Model model) {
		String name = request.getParameter("name");
		int career = Integer.valueOf(request.getParameter("career"));
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String[] lang = request.getParameterValues("lang");
		
		Dev dev = new Dev(0, name, career, email, gender, lang);	
		System.out.println(dev);
		log.info("dev = {}", dev);
		
		
//		request.setAttribute("dev", dev);
		model.addAttribute("dev", dev); // model 객체를 통해 jsp에 객체, 값 등을 전달
		
		return "demo/devResult";
	}
	
	@RequestMapping(value="/dev2.do", method=RequestMethod.POST)
	public String dev2(
				@RequestParam(value = "name") String name,
				@RequestParam(value = "career") int career,
				@RequestParam(value = "email") String email,
				@RequestParam(value = "gender", defaultValue = "M") String gender,
				@RequestParam(value = "lang", required = false) String[] lang,
				Model model
			) {
		Dev dev = new Dev(0, name, career, email, gender, lang);
		log.info("{}", dev);
		model.addAttribute("dev", dev);
		
		return "demo/devResult";
	}
	
		/**
		 * 커맨드객체 : 사용자입력값과 일치하는 필드에 값대입
		 * 커맨드객체는 자동으로 model 속성으로 지정
		 * 커맨드객체는 @ModelAttribute 쓰지 않아도 자도응로 지정되고,
		 * 쓰면 참조되는 이름을 바꿀 수 있음
		 * @param dev
		 * @return
		 */
	//	@RequestMapping(value="/dev3.do", method=RequestMethod.POST)
		@PostMapping("/dev3.do")
		public String dev3(@ModelAttribute("ddddev") Dev dev) {
			log.info("{}", dev);
			return "demo/devResult";
		}
	
	@PostMapping("/insertDev.do")
	// RedirectAttributes는 redirect 요청이 오면, 
	// 저장한 메세지를 꺼내서 보여주게끔 한다.
	public String insertDev(Dev dev, RedirectAttributes redirectAttr) {
		log.info("{}", dev);
		//1. 업무로직
		int result = demoService.insertDev(dev);
		//2. 사용자피드백 및 리다이렉트(DML)
		String msg = result > 0 ? "Dev 등록 성공!" : "Dev 등록 실패!";
		log.info("처리결과 : {}", msg);
		//리다이렉트 후 사용자피드백 전달하기
		redirectAttr.addFlashAttribute("msg",msg);
		
		return "redirect:/"; // /spring
	}
	
	@GetMapping("/devList.do")
	public String devList(Model model) {
		//1. 업무로직
		List<Dev> list = demoService.selectDevList();
		log.info("list = {}", list);
		
		//2. jsp 위임
		model.addAttribute("list", list);
		
		return "demo/devList";
	}
	
	@GetMapping("/updateDev.do")
	public String updateDev(@RequestParam(required = true) int no, Model model) {
		//1. 업무로직
		Dev dev = demoService.selectDevOne(no);
		log.info("dev = {}", dev);
		
		//2. jsp위임
		model.addAttribute("dev", dev);
		model.addAttribute("list", Arrays.asList(dev.getLang()));
		return "demo/devUpdateDev";
	}
	
	@PostMapping("/updateDev.do")
	public String updateDev(Dev dev, RedirectAttributes redirectAttr) {
//		log.info("post = {}", dev);
		//1. 업무로직 : Dev 1명 수정
		int result = demoService.updateDevOne(dev);
		log.info("dev = {}", dev);
		
		//2. 리다이렉트 및 사용자 피드백
		String msg = result > 0 ? "Dev 수정 성공!" : "Dev 수정 실패!";
		log.info("처리결과 : {}", msg);
		redirectAttr.addFlashAttribute("msg",msg);
		
		return "redirect:/demo/devList.do";
	}
	
	@PostMapping("/deleteDev.do")
	public String deleteDev(@RequestParam(required = true) int no, RedirectAttributes redirectAttr) {
//		log.info("post = {}", dev);
		//1. 업무로직 : Dev 1명 삭제
		int result = demoService.deleteDevOne(no);
		
		//2. 리다이렉트 및 사용자 피드백
		String msg = result > 0 ? "Dev 삭제 성공!" : "Dev 삭제 실패!";
		log.info("처리결과 : {}", msg);
		redirectAttr.addFlashAttribute("msg",msg);
		
		return "redirect:/demo/devList.do";
	}
}




