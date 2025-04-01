package com.sinuedu.board.lecture.model.vo;

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
public class Lecture {
	private int lecNo;
	private String lecTitle;
	private String lecSubtitle;
	private String lecDesc;
	private String cgName;
	private String lecImg;
	private int totalChap;
	private double svgRate;
	private int progressRate;
	private String bookmarkCheck;
}
