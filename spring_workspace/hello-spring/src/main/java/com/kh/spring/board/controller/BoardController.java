package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.common.HelloSpringUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {

	@Autowired
	private BoardService boardService;

	@GetMapping("/boardList.do")
	public void boardList(@RequestParam(defaultValue = "1") int cPage, 
			Model model,
			HttpServletRequest request) {
		//1. 사용자입력값
		int numPerPage = 5;
		log.debug("cPage = {}", cPage);
		Map<String, Object> param = new HashMap<>();
		param.put("numPerPage", numPerPage);
		param.put("cPage", cPage);
		
		//2. 업무로직
		//a. contents 영역 : mybatis의 RowBounds
		List<Board> list = boardService.selectBoardList(param);
		log.debug("list = {}", list);
		model.addAttribute("list", list);
		
		//b. pagebar 영역
		int totalContents = boardService.getTotalContents();
		String url = request.getRequestURI();
		log.debug("totalContents : {}", totalContents);
		log.debug("url : {}", url);
		String pageBar = HelloSpringUtils.getPageBar(totalContents, cPage, numPerPage, url);
		
		//3. jsp 처리 위임
		model.addAttribute("list", list);
		model.addAttribute("pageBar", pageBar);
	}
	
	@GetMapping("/boardForm.do")
	public void boardForm() {
		
	}
	
	/**
	 * 1. form[enctype=multipart/form-data]
	 * 2. @RequestParam MultipartFile upFile
	 * 3. 서버컴퓨터에 파일 저장 
	 * 	- saveDirectory(/resources/upload/board/20210427_12345_234.jpg)
	 *  - 파일명 재지정
	 * 4. DB attachment에 저장된 파일정보 등록
	 * 	- Attachment 객체 --> List<Attachment> --> board.setAttachList
	 * @param board
	 * @param redirectAttr
	 * @return
	 */
	@PostMapping("/boardEnroll.do")
	public String insertBoard(@ModelAttribute Board board, 
							@RequestParam(value="upFile", required=false) MultipartFile[] upFiles,
							HttpServletRequest request,
							RedirectAttributes redirectAttr) {

		try {
			log.debug("board = {}", board);
			//0. 파일 저장
			//저장경로
			//pageContext - request - session - application
			//ServletContext는 application scope
			String saveDirectory = 
					request.getServletContext().getRealPath("/resources/upload/board");
			//File객체를 통해서 directory 가능
			File dir = new File(saveDirectory);
			if(!dir.exists())
				dir.mkdirs(); 	// 복수개 폴더 생성 가능
			
			//복수개의 Attachment 객체를 담을 list 생성
	         List<Attachment> attachList = new ArrayList<>();
			
			for(MultipartFile upFile : upFiles) {
				if(upFile.isEmpty() || upFile.getSize() == 0)//둘 다 같은 의미임
					continue;
				log.debug("upFile = {}", upFile);
				log.debug("upFile.name = {}", upFile.getOriginalFilename());
				log.debug("upFile.size = {}", upFile.getSize());
				//저장할 파일명 생성
				File renamedFile = HelloSpringUtils.getRenamedFile(saveDirectory, upFile.getOriginalFilename());
				//파일 저장
				upFile.transferTo(renamedFile);
				//Attachment 객체생성
				Attachment attach = new Attachment();
				attach.setOriginalFileName(upFile.getOriginalFilename());
				attach.setRenamedFileName(renamedFile.getName());
				attachList.add(attach);
			}
			
			//1. 업무로직
			board.setAttachList(attachList);
			int result = boardService.insertBoard(board);
			String msg = "게시글 등록 성공!";

			//2. 사용자피드백 준비 및 리다이렉트
			redirectAttr.addFlashAttribute("msg",msg);			
		}catch(IOException | IllegalStateException e) {
			log.error("첨부파일 저장 오류!", e);
			throw new BoardException();
		}catch(Exception e) {
			//1. 로깅작업
			log.error("게시물 등록 오류", e);
			//2. 다시 spring container에 던질 것.
			throw e;
		}
		
		return "redirect:/board/boardList.do";
	}
	
	@GetMapping("/boardDetail.do")
	public void boardDetail(@RequestParam int no, Model model) {
		Board board = boardService.selectBoard(no);
		log.debug("detail board = {}", board);
	
		model.addAttribute("board", board);
	}
}







