package com.sinuedu.board.qna.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class reply {
	   private int repNo;
	   private String repComment;
	   private String userNick;
	   private int userNo;
	   private int qnaNo;

	// vo에는 이거 두줄 딸깍인데 이거 안쓰고 불러오려니까
	// 이거 두줄 쓴거의 20배로 써야해서 그냥 추가했어유..
	private String qnaTitle; // 질문글 제목 (JOIN해서 가져옴)
	private String cgName;   // 카테고리명 (JOIN해서 가져옴)
}
