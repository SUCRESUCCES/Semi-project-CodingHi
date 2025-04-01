package com.sinuedu.board.qna.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Qna {
	   private int qnaNo;
	   private String qnaTitle;
	   private String qnaDetail;
	   private int writer;
	   private String userNick;
	   private String cgName;
	   private Date createDate;
	   private Date updateDate;
	   private int views;
	   private int cgNo;
	   private String notice;

	   // 추가한거
	   private int replyCount;
}
