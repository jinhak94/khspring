package com.kh.spring.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardDao boardDao;

	@Override
	public List<Board> selectBoardList(Map<String, Object> param) {
		return boardDao.selectBoardList(param);
	}

	@Override
	public int getTotalContents() {
		return boardDao.getTotalContents();
	}

	/**
	 * @Transactional
	 * - class level : 모든 메소드 실행결과 Runtime 예외가 던져지면, rollback.
	 * - method level : 해당 메소드 실행결과 Runtime 예외가 던져지면, rollback.
	 */
	
	//@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertBoard(Board board) {
		int result = 0;
		//1. board객체 등록
		result = boardDao.insertBoard(board);
		log.debug("board.no = {}", board.getNo());
		//2. attachment 객체 등록
		//insert into attachment(no, board_no, original_filename, renamed_filename)
		//values(seq_attachment_no.nextval, #{boardNo}, #{originalFileName}, #{renamedFileName}
		if(!board.getAttachList().isEmpty()) {
			for(Attachment attach : board.getAttachList()) {
				attach.setBoardNo(board.getNo());
				result = boardDao.insertAttachment(attach);
			}
		}
		return result;
	}

	@Override
	public Board selectBoard(int no) {
		Board board = null;
		
		board = boardDao.selectBoard(no);
		log.debug("board = {}", board);
		
		List<Attachment> attachList = null;
		attachList = boardDao.selectAttachList(no);
		board.setAttachCount(attachList.size());
		board.setAttachList(attachList);
		
		return board;
	}

	@Override
	public Board selectOneBoardCollection(int no) {
		return boardDao.selectOneBoardCollection(no);
	}

	@Override
	public Attachment selectOneAttachment(int no) {
		return boardDao.selectOneAttachment(no);
	}


	@Override
	public List<Map<String, Object>> selectBoardByTitle(String searchTitle) {
		// TODO Auto-generated method stub
		return boardDao.selectBoardByTitle(searchTitle);
	}
	
	//service에서 board, attachment 갔다오고
	//하나의 객체를 리턴
	
}
