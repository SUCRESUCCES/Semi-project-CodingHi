package com.sinuedu.user.manager.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sinuedu.board.lecture.model.vo.Category;
import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Image;
import com.sinuedu.board.lecture.model.vo.Lecture;
import com.sinuedu.user.member.model.vo.Member;

@Mapper
@Repository
public interface ManagerMapper {

	List<Member> userList();

	List<Category> categoryList();

	List<Lecture> lectureList();
	
	List<Chapter> chapterList();

	int deleteChapter(int chapNo);

	int insertChapter(Chapter chapter);

	Chapter getChapterByTitle(int chapNo);

	int updateChapter(Chapter chapter);
	
	int updateAdminStatus(@Param("userId") String userId, @Param("adminStatus") String adminStatus);

	int updateUserStatus(@Param("userId") String userId, @Param("status") String status);

	int deleteViewChapter(int chapNo);

	int insertLecture(Lecture lec);

	int insertImage(Image i);

	int deleteAllChapter(int lecNo);

	int deleteLecture(int lecNo);

	int updateLecture(Lecture lec);

	int updateImage(Image i);

}