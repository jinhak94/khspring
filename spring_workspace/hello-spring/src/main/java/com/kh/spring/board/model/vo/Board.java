package com.kh.spring.board.model.vo;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {

	//테이블 구조
	private int no;
	private String title;
	private String memberId;
	private String content;
	private Date regDate;
	//시분초 정보가 필요하면 util.date
	//아니면 sql.date로 ,,, 둘 다 가능0
	private int readCount;
	
	//테이블 구조와 별개
	private int attachCount;		//첨부파일 개수
	private List<Attachment> attachList;
}
