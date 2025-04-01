package com.sinuedu.home.controller;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.sinuedu.board.lecture.controller.LectureController;
import com.sinuedu.board.lecture.model.service.LectureService;
import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Lecture;
import com.sinuedu.home.service.HomeService;
import com.sinuedu.user.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class HomeController {

    private final HomeService homeService;
    private final LectureService cService;
    private final LectureController cController;


    @SuppressWarnings("unchecked")
	@GetMapping("/home")
    public ModelAndView homePage(ModelAndView mav, HttpSession session) {
    	
    	HashMap<String, Integer> map = new HashMap<>();
    	
        Member loginUser = (Member) session.getAttribute("loginUser");
        int userNo = 0;
        if(loginUser != null) {
        	userNo = loginUser.getUserNo();
        }
        
        Map<String, Object> homeData = homeService.getHomeData(userNo);
        
        //popularLectures 키에 담겨져있는 Lecture 을 불러오는 것
        ArrayList<Lecture> list = new ArrayList<>();
        list.addAll((Collection<? extends Lecture>) homeData.get("popularLectures"));
        for(Lecture lec : list) {
        	int lecNo = lec.getLecNo();
			int capCount = cService.chapterCount(lecNo);
			lec.setTotalChap(capCount);
			
			ArrayList<Chapter> cList = cService.selectLecture(lecNo);
			
			lec.setSvgRate(cController.svgRate(cList)); 
			
			//유저별 강의 진도율 표시
			lec.setProgressRate(cController.progressRate(capCount, userNo, lecNo));
//			System.out.println(lec);
			
			map.put("userNo", userNo);
			map.put("lecNo", lecNo);
			
			int bookmarkCheck = cService.countBookmark(map);
			if(bookmarkCheck == 1) {
				lec.setBookmarkCheck("Y");
			}else {
				lec.setBookmarkCheck("N");
			}
        }
        
        
        mav.addAllObjects(homeData);


        mav.setViewName("views/home");

        return mav;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Object loginUser = session.getAttribute("loginUser"); // 세션에서 가져오기
        model.addAttribute("loginUser", loginUser);
        return "index";
    }
}
