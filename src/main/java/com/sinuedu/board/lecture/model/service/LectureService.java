package com.sinuedu.board.lecture.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.sinuedu.board.lecture.model.mapper.LectureMapper;
import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Image;
import com.sinuedu.board.lecture.model.vo.Lecture;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureService {
	
	private final LectureMapper cMapper;

	public ArrayList<Lecture> selectLectureList(Integer lecNo) {
		return cMapper.selectLectureList(lecNo);
	}

	public int chapterCount(int lecNo) {
		return cMapper.chapterCount(lecNo);
	}

	public ArrayList<Chapter> selectLecture(int lecNo) {
		return cMapper.selectLecture(lecNo);
	}

	public Chapter selectChapter(int chapNo, int userNo) {
		if(userNo != 0) {
			int result = cMapper.updateCount(chapNo);
		}
		return cMapper.selectChapter(chapNo);
	}

	public int rating(HashMap<String, Integer> map) {
		return cMapper.rating(map);
	}

	public int viewChapter(HashMap<String, Integer> map) {
		return cMapper.viewChapter(map);
	}

	public int dupViewChapter(HashMap<String, Integer> map) {
		return cMapper.dupViewChapter(map);
	}

	public int chapRateAvg(HashMap<String, Integer> map) {
		return cMapper.chapRateAvg(map);
	}

	public int userProgressRate(HashMap<String, Integer> map) {
		return cMapper.userProgressRate(map);
	}

	public ArrayList<Lecture> selectCategory(String cgName) {
		return cMapper.selectCategory(cgName);
	}

	public int countBookmark(HashMap<String, Integer> map) {
		return cMapper.countBookmark(map);
	}

	public int insertBookmark(HashMap<String, Integer> map) {
		return cMapper.insertBookmark(map);
	}

	public int deleteBookmark(HashMap<String, Integer> map) {
		return cMapper.deleteBookmark(map);
	}

	public Lecture bookmarkCategory(HashMap<String, Integer> map) {
		return cMapper.bookmarkCategory(map);
	}

	public ArrayList<Image> selectImageList(Integer lecNo) {
		return cMapper.selectImageList(lecNo);
	}

	public Image selectImage(int lecNo) {
		return cMapper.selectImage(lecNo);
	}

	
}
