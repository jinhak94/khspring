package com.kh.spring.board.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.board.model.vo.Board;

@Repository
public class BoardDaoImpl implements BoardDao {

	@Autowired
	private SqlSessionTemplate session;

	/**
	 * RowBounds : mybatis가 제공하는 paging 기능
	 * - offset : 건너뛸 행수
	 * - limit : 한 페이지당 게시물 수
	 */
	
	@Override
	public List<Board> selectBoardList(Map<String, Object> param) {
//		return session.selectBoardList("board.selectBoardList", param);
		int cPage = (int)param.get("cPage");
		int limit = (int)param.get("numPerPage");
		int offset = 0;
		RowBounds rowBounds = new RowBounds(offset, limit);
		return session.selectList("board.selectBoardList", null, rowBounds);
	}

	@Override
	public int getTotalContents() {
		return session.selectOne("board.getTotalContents");
	}

	@Override
	public int boardEnroll(Board board) {
		return session.insert("board.boardEnroll", board);
	}	
}
