package com.kh.spring.board.model.dao;

import java.util.List;
import java.util.Map;

import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;

public interface BoardDao {

	List<Board> selectBoardList(Map<String, Object> param);

	int getTotalContents();

	int insertBoard(Board board);

	int insertAttachment(Attachment attach);

	Board selectBoard(int no);

	List<Attachment> selectAttachList(int no);

	Board selectOneBoardCollection(int no);

	Attachment selectOneAttachment(int no);

	List<String> checkBoardList(String searchTitle);

	List<Map<String, Object>> selectBoardByTitle(String searchTitle);

}
