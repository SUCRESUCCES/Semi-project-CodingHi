package com.sinuedu.board.lecture.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Chapter {
	private int chapNo;
	private int lecChapNum;
	private String lecTitle;
	private String chapTitle;
	private String chapDetail;
	private Date createDate;
	private Date updateDate;
	private int views;
	private int lecNo;
	private String cgName;
	private int chapRate;
	
}
