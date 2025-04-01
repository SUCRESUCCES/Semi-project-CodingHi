package com.sinuedu.board.lecture.model.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Image;
import com.sinuedu.board.lecture.model.vo.Lecture;

@Mapper
public interface LectureMapper {

	ArrayList<Lecture> selectLectureList(Integer lecNo);

	int chapterCount(int lecNo);

	ArrayList<Chapter> selectLecture(int lecNo);

	Chapter selectChapter(int chapNo);

	int rating(HashMap<String, Integer> map);

	int viewChapter(HashMap<String, Integer> map);

	int dupViewChapter(HashMap<String, Integer> map);

	int chapRateAvg(HashMap<String, Integer> map);

	// 아래로 추가한거
	ArrayList<Lecture> PopularLectures();

	int userProgressRate(HashMap<String, Integer> map);

	int updateCount(int chapNo);

	ArrayList<Lecture> selectCategory(String cgName);

	int countBookmark(HashMap<String, Integer> map);

	int insertBookmark(HashMap<String, Integer> map);

	int deleteBookmark(HashMap<String, Integer> map);

	Lecture bookmarkCategory(HashMap<String, Integer> map);

	ArrayList<Image> selectImageList(Integer lecNo);

	Image selectImage(int lecNo);

	

}
