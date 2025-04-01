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
public class Image {
	private int imgNo;
	private int refLecNo;
	private String imgName;
	private String imgRename;
	private String imgPath;
}
