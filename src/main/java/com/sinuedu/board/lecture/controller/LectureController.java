package com.sinuedu.board.lecture.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.sinuedu.board.lecture.model.exception.LectureException;
import com.sinuedu.board.lecture.model.service.LectureService;
import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Image;
import com.sinuedu.board.lecture.model.vo.Lecture;
import com.sinuedu.user.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
@RequestMapping("/lecture")
public class LectureController {
	
	private final LectureService cService;
	
	@GetMapping("list")
	public ModelAndView selectLectureList(ModelAndView mv, HttpSession session) {
		
		HashMap<String, Integer> map = new HashMap<>();
		
		int userNo = 0;
		if(session.getAttribute("loginUser") != null){
			userNo = ((Member)session.getAttribute("loginUser")).getUserNo();
		}
		ArrayList<Lecture> list = cService.selectLectureList(null);
		ArrayList<Image> iList = cService.selectImageList(null);
		
		for(Lecture lec : list) {
			int lecNo = lec.getLecNo();
			int capCount = cService.chapterCount(lecNo);
			lec.setTotalChap(capCount);
			
			ArrayList<Chapter> cList = cService.selectLecture(lecNo);
			
			lec.setSvgRate(svgRate(cList)); 
			
			//유저별 강의 진도율 표시
			lec.setProgressRate(progressRate(capCount, userNo, lecNo));
			
			map.put("userNo", userNo);
			map.put("lecNo", lecNo);
			
			int bookmarkCheck = cService.countBookmark(map);
			if(bookmarkCheck == 1) {
				lec.setBookmarkCheck("Y");
			}else {
				lec.setBookmarkCheck("N");
			}
		}
		
		mv.addObject("list", list).addObject("iList", iList).setViewName("category");
		
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView selectLecture(@PathVariable("id") int lecNo, ModelAndView mv,
									  HttpSession session) {
		System.out.println(lecNo);
		HashMap<String, Integer> map = new HashMap<>();
		ArrayList<Chapter> cList = cService.selectLecture(lecNo);
		int capCount = cService.chapterCount(lecNo);
		ArrayList<Lecture> lList = cService.selectLectureList(lecNo);
		Lecture lec = lList.get(0);
		Image img = new Image();
		
		ArrayList<Image> iList = cService.selectImageList(lecNo);
		if(!iList.isEmpty()) {
			img = iList.get(0);
		}
		
		int userNo = 0;
		if(session.getAttribute("loginUser") != null){
			userNo = ((Member)session.getAttribute("loginUser")).getUserNo();
		}
		
		for(int i =1 ; i<=cList.size() ; i++) {
			cList.get(i-1).setLecChapNum(i);
		}
		
		double svgRate = svgRate(cList);
		int progressRate = progressRate(capCount, userNo, lecNo);
		
		map.put("userNo", userNo);
		map.put("lecNo", lecNo);
		
		int bookmarkCheck = cService.countBookmark(map);
		if(bookmarkCheck == 1) {
			lec.setBookmarkCheck("Y");
		}else {
			lec.setBookmarkCheck("N");
		}
		
		mv.addObject("lecNo",lecNo).addObject("svgRate", svgRate).addObject("progressRate", progressRate);
		mv.addObject("lec", lec).addObject("img", img).addObject("cList", cList).addObject("capCount", capCount).setViewName("postlist");
		return mv;
	}
	
	//Lecture 평균 구하는 메소드
	public double svgRate(ArrayList<Chapter> list) {
		
		int sumRate = 0;
		double svgRate = 0;
		
		ArrayList<Chapter> rateNotNull = new ArrayList<>();
		for(Chapter c : list) {
			sumRate += c.getChapRate();
			
			//평점을 준사람은 무조건 1점부터
			if(c.getChapRate() != 0) {
				rateNotNull.add(c);
			}
		}
	
		
		svgRate = (double)sumRate/rateNotNull.size();
		svgRate = Double.parseDouble(String.format("%.1f", svgRate));
		if(Double.isNaN(svgRate)) {
			svgRate = 0;
		}
		return svgRate;
	}
	
	//유저별 강의 진도율 표시
	public int progressRate(int capCount, int userNo, int lecNo) {
		int result = 0;
		
		if(capCount > 0 && userNo >0) {
			HashMap<String, Integer> map = new HashMap<>();
			map.put("userNo", userNo);
			map.put("lecNo", lecNo);
			
			int userProgressRate = cService.userProgressRate(map);
			result = (int) ((userProgressRate / (double) capCount) * 100);
		}
		return result;
	}
	
	@PostMapping("/{lNo}/{cNo}")
	public ModelAndView selectChapter(@PathVariable("lNo") int lecNo, @PathVariable("cNo") int lecChapNum, 
									  @RequestParam("chapNo") int chapNo,ModelAndView mv,
									  HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		int userNo = 0;
		if(loginUser != null) {
			userNo = loginUser.getUserNo();
			
			HashMap<String, Integer> map = new HashMap<>();
			map.put("chapNo", chapNo);
			map.put("userNo", userNo);
			int result1 = cService.dupViewChapter(map);
			if(result1 == 0) {
				cService.viewChapter(map);
			}
		}else {
			throw new LectureException("로그인이 되어있지 않습니다");
		}
		
		
		Chapter chapter = cService.selectChapter(chapNo, userNo);
		chapter.setLecChapNum(lecChapNum);
		
		mv.addObject("chapNo", chapNo);
		mv.addObject("chapter", chapter).addObject("lecChapNum",lecChapNum).addObject("lecNo",lecNo).setViewName("post");
		
		return mv;
	}
	
	@GetMapping("rating")
	@ResponseBody
	public int rating(@RequestParam("rating") int rating, @RequestParam("chapNo") int chapNo,
					   HttpSession session) {
		System.out.println(1);
		Member loginUser = (Member)session.getAttribute("loginUser");
		int userNo = loginUser.getUserNo();
		System.out.println(loginUser);
		System.out.println("chapNo : " + chapNo);
		System.out.println("rating : " + rating);
		
		HashMap<String, Integer> map = new HashMap<>();
		map.put("chapNo", chapNo);
		map.put("rating", rating);
		map.put("userNo", userNo);
		
		int result = cService.rating(map);
		if(result >0) {
			cService.chapRateAvg(map);
			return result;
		}else {
			throw new LectureException("별점이 추가되지 않았습니다");
		}
		
	}
	
	@GetMapping("/category/{spanVal}")
	public ModelAndView selectCategory(@PathVariable("spanVal") String cgName,
										ModelAndView mv, HttpSession session) {
		System.out.println(cgName);
		HashMap<String, Integer> map = new HashMap<>();
		
		ArrayList<Lecture> list = new ArrayList<Lecture>();
		ArrayList<Lecture> bookmarkList = new ArrayList<Lecture>();
		ArrayList<Image> iList = new ArrayList<Image>();
		
		
		int userNo = 0;
		if(session.getAttribute("loginUser") != null){
			userNo = ((Member)session.getAttribute("loginUser")).getUserNo();
		}
		
		if(cgName.equals("BOOKMARK")) {
			list = cService.selectLectureList(null);
		}else {
			list = cService.selectCategory(cgName);
		}
		
		System.out.println(userNo);
		
		for(Lecture lec : list) {
			int lecNo = lec.getLecNo();
			map.put("userNo", userNo);
			map.put("lecNo", lecNo);
			
			iList.add(cService.selectImage(lecNo));
			
			System.out.println(lecNo);
			if(cgName.equals("BOOKMARK")) {
				Lecture l = cService.bookmarkCategory(map);
				if(l != null) {
					bookmarkList.add(l);
				}
			}else {
				int capCount = cService.chapterCount(lecNo);
				lec.setTotalChap(capCount);
				
				ArrayList<Chapter> cList = cService.selectLecture(lecNo);
				
				lec.setSvgRate(svgRate(cList)); 
				
				//유저별 강의 진도율 표시
				lec.setProgressRate(progressRate(capCount, userNo, lecNo));
				
				
				
				int bookmarkCheck = cService.countBookmark(map);
				if(bookmarkCheck == 1) {
					lec.setBookmarkCheck("Y");
				}else {
					lec.setBookmarkCheck("N");
				}	
			}
		}
		System.out.println(iList);
		
		System.out.println("bookmarkList : " + bookmarkList);
		
		if(cgName.equals("BOOKMARK")) {
			for(Lecture lec : bookmarkList) {
				int lecNo = lec.getLecNo();
				map.put("userNo", userNo);
				map.put("lecNo", lecNo);
				
				int capCount = cService.chapterCount(lecNo);
				lec.setTotalChap(capCount);
				
				ArrayList<Chapter> cList = cService.selectLecture(lecNo);
				
				lec.setSvgRate(svgRate(cList)); 
				
				//유저별 강의 진도율 표시
				lec.setProgressRate(progressRate(capCount, userNo, lecNo));
				
				
				
				int bookmarkCheck = cService.countBookmark(map);
				if(bookmarkCheck == 1) {
					lec.setBookmarkCheck("Y");
				}else {
					lec.setBookmarkCheck("N");
				}	
			}
			mv.addObject("list", bookmarkList).addObject("iList", iList);
		}else {
			mv.addObject("list", list).addObject("iList", iList);
		}
		
		mv.setViewName("category");
		
//		System.out.println(mv2);
		return mv;
	}
	
	@GetMapping("insertBookmark")
	@ResponseBody
	public int insertBookmark(@RequestParam("lecNo") int lecNo, HttpSession session) {
		
		int userNo = ((Member)session.getAttribute("loginUser")).getUserNo();
		HashMap<String, Integer> map = new HashMap<>();
		map.put("lecNo", lecNo);
		map.put("userNo", userNo);
		int result = 0;
		int bookmarkCheck = cService.countBookmark(map);
		if(bookmarkCheck == 0) {
			result = cService.insertBookmark(map);
		}
		
		return result;
	}
	
	@GetMapping("deleteBookmark")
	@ResponseBody
	public int deleteBookmark(@RequestParam("lecNo") int lecNo, HttpSession session) {
		
		int userNo = ((Member)session.getAttribute("loginUser")).getUserNo();
		HashMap<String, Integer> map = new HashMap<>();
		map.put("lecNo", lecNo);
		map.put("userNo", userNo);
		int result = 0;
		int bookmarkCheck = cService.countBookmark(map);
		if(bookmarkCheck == 1) {
			result = cService.deleteBookmark(map);
		}
		
		return result;
	}
	
	
}
