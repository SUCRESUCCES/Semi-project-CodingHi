package com.sinuedu.home.service;

import com.sinuedu.board.lecture.model.mapper.LectureMapper;
import com.sinuedu.board.qna.model.mapper.QnaMapper;
import com.sinuedu.board.qna.model.vo.Qna;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import java.util.HashMap;
import java.util.Map;

@Service // <== 이 부분이 꼭 필요함
public class HomeService {

    private final LectureMapper lectureMapper;
    private final QnaMapper qnaMapper;


    public HomeService(LectureMapper lectureMapper, QnaMapper qnaMapper) {
        this.lectureMapper = lectureMapper;
        this.qnaMapper = qnaMapper;
    }


    public Map<String, Object> getHomeData(int userNo) {
        Map<String, Object> homeData = new HashMap<>();
        homeData.put("popularLectures", lectureMapper.PopularLectures());
        // HTML 태그 제거한 QnA 데이터 저장
        ArrayList<Qna> PopularQna = qnaMapper.PopularQna();
        List<Qna> tagFilteredQnaList = PopularQna.stream()
                .map(qna -> {
                    qna.setQnaDetail(Jsoup.parse(qna.getQnaDetail()).text()); // HTML 태그 제거
                    return qna;
                }).collect(Collectors.toList());

        homeData.put("popularQna", tagFilteredQnaList);

        homeData.put("myRecentQna", qnaMapper.getRecentQnaByUser(userNo)); // 내가 쓴 질문
        homeData.put("myRecentComments", qnaMapper.getRecentCommentsByUser(userNo));
        return homeData;
    }
}
