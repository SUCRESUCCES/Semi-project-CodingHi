package com.sinuedu.user.manager.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sinuedu.board.lecture.model.vo.Category;
import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Image;
import com.sinuedu.board.lecture.model.vo.Lecture;
import com.sinuedu.user.manager.model.mapper.ManagerMapper;
import com.sinuedu.user.member.model.vo.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManagerService {

	private final ManagerMapper mapper;

	public List<Member> userList() {
		return mapper.userList();
	}

	public List<Category> categoryList() {
		return mapper.categoryList();
	}

	public List<Lecture> lectureList() {
		return mapper.lectureList();
	}

	public List<Chapter> chapterList() {
		return mapper.chapterList();
	}

	public int deleteChapter(int chapNo) {
		return mapper.deleteChapter(chapNo);
	}

	public int insertChapter(Chapter chapter) {
		return mapper.insertChapter(chapter);
	}

	public Chapter getChapterByTitle(int chapNo) {
		return mapper.getChapterByTitle(chapNo);
	}

	public int updateChapter(Chapter chapter) {
		return mapper.updateChapter(chapter);
	}

	public int updateAdminStatus(String userId, String adminStatus) {
	    return mapper.updateAdminStatus(userId, adminStatus);
	}

	public int updateUserStatus(String userId, String status) {
	    return mapper.updateUserStatus(userId, status);
	}

	public int deleteViewChapter(int chapNo) {
		return mapper.deleteViewChapter(chapNo);
	}

	public int insertLecture(Lecture lec) {
		return mapper.insertLecture(lec);
	}

	public int insertImage(Image i) {
		return mapper.insertImage(i);
	}

	public int deleteAllChapter(int lecNo) {
		return mapper.deleteAllChapter(lecNo);
	}

	public int deleteLecture(int lecNo) {
		return mapper.deleteLecture(lecNo);
	}

	public int updateLecture(Lecture lec) {
		return mapper.updateLecture(lec);
	}

	public int updateImage(Image i) {
		return mapper.updateImage(i);
	}
}