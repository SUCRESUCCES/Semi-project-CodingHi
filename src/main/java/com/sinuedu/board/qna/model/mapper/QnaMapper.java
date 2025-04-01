package com.sinuedu.board.qna.model.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.sinuedu.board.lecture.model.vo.Category;
import com.sinuedu.board.qna.model.vo.Qna;
import com.sinuedu.board.qna.model.vo.reply;

@Mapper
public interface QnaMapper {

	int getListCount(HashMap<String,String> map);

	ArrayList<Qna> selectNoticeBoardList(HashMap<String, String> map, RowBounds rowBounds);
	
	ArrayList<Qna> selectQnaBoardList(HashMap<String, String> map, RowBounds rowBounds);
	
	ArrayList<reply> selectReply(int rNo);
	
	Qna selectBoard(int qNo);

	int insertBoard(Qna q);

	int updateCount(int qNo);

	int insertReply(reply r);

	ArrayList<Category> selectCategory();

	int updateBoard(Qna q);

	int noticeBoard(Qna q);

	ArrayList<Qna> searchList(String value);

	ArrayList<Qna> selectResult();

	int deletePost(int qNo);

	ArrayList<Qna> selectResult(List<Qna> result);

	// 아래로 추가한거

	ArrayList<Qna> PopularQna();

	ArrayList<Qna> getRecentQnaByUser(int userNo);

	ArrayList<reply> getRecentCommentsByUser(int userNo);

//	ArrayList<reply> selectReplyList(int repNo);
	
	int deleteReply(int rId);

	int updateReply(reply r);

}
