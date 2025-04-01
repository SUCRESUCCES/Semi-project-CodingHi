package com.sinuedu.board.qna.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.sinuedu.board.lecture.model.vo.Category;
import com.sinuedu.board.qna.model.mapper.QnaMapper;
import com.sinuedu.board.qna.model.vo.PageInfo;
import com.sinuedu.board.qna.model.vo.Qna;
import com.sinuedu.board.qna.model.vo.reply;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaService {

	private final QnaMapper mapper;

	public int getListCount(HashMap<String, String> map) {
		return mapper.getListCount(map);
	}
	
	//All 불러오기
	public ArrayList<Qna> selectAllBoardList(HashMap<String, String> map, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		// 공지글 5개 가져오기
		ArrayList<Qna> noticePosts = mapper.selectNoticeBoardList(map, rowBounds);
		
		// 나머지 글 가져오기
		ArrayList<Qna> nonNoticePosts = mapper.selectQnaBoardList(map, rowBounds);
		
		//최종 글 리턴
		ArrayList<Qna> resultPosts = new ArrayList<>();
		
		if("Y".equals(map.get("category"))){
			return noticePosts;
		}else if ("N".equals(map.get("category"))){
			return nonNoticePosts;
		}else {
			
			resultPosts.addAll(noticePosts);
			resultPosts.addAll(nonNoticePosts);
			
			return resultPosts;
		}
	}
	

	public ArrayList<reply> selectReply(int rNo) {
		return mapper.selectReply(rNo);
	}

	public int insertBoard(Qna q) {
		return mapper.insertBoard(q);
	}

	public Qna selectBoard(int qNo, String id) {
		Qna q = mapper.selectBoard(qNo);
		if (q != null && id != null && !q.getUserNick().equals(id)) {
			int result = mapper.updateCount(qNo);
			q.setViews(q.getViews() + 1);
		} else {
		}
		return q;
	}

	public int insertReply(reply r) {
		return mapper.insertReply(r);
	}

	public ArrayList<Category> selectCategory() {
		return mapper.selectCategory();
	}

	public int updateBoard(Qna q) {
		return mapper.updateBoard(q);
	}

	public int deletePost(int qNo) {
		return mapper.deletePost(qNo);
	}

	public int noticeBoard(Qna q) {
		return mapper.noticeBoard(q);
	}

	public ArrayList<Qna> selectResult(List<Qna> result) {
		return mapper.selectResult(result);
	}

//	public ArrayList<reply> selectReplyList(int repNo) {
//		return mapper.selectReplyList(repNo);
//	}

	public int deleteReply(int rId) {
		return mapper.deleteReply(rId);
	}

	public int updateReply(reply r) {
		return mapper.updateReply(r);
	}

	

}
