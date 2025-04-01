package com.sinuedu.board.qna.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinuedu.board.lecture.model.vo.Category;
import com.sinuedu.board.qna.exception.QnaException;
import com.sinuedu.board.qna.model.service.QnaService;
import com.sinuedu.board.qna.model.vo.PageInfo;
import com.sinuedu.board.qna.model.vo.Qna;
import com.sinuedu.board.qna.model.vo.reply;
import com.sinuedu.common.Pagination;
import com.sinuedu.user.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
@RequestMapping("/qna")
public class QnaController {

	private final QnaService bService;

	@GetMapping("list")
	public String selectList(@PathVariable(value="category", required = false) String category, 
							 @RequestParam(value = "page", defaultValue = "1") int currentPage,
							 @RequestParam(value="condition", required = false) String condition,
							 @RequestParam(value = "search", required = false) String search,
							 HttpServletRequest request,  Model m) {

		// 검색 조건을 담은 HashMap 생성
		HashMap<String, String> map = new HashMap<>();
		map.put("search", search);
		map.put("condition", condition);
		
		// 검색 조건에 맞는 전체 게시글 개수 조회
		int listCount = bService.getListCount(map);
		// 페이징 정보 설정
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		// 조건에 맞는 글 목록 조회, 페이징 정보도 함께 전달해서 해당 페이지에 맞는 게시물만 조회
		ArrayList<Qna> list = bService.selectAllBoardList(map,pi);
		
		// 사용할 데이터를 뷰에 전달
		m.addAttribute("search", search).addAttribute("condition", condition);
		m.addAttribute("list", list).addAttribute("pi", pi);
		m.addAttribute("loc", request.getRequestURI());
		m.addAttribute("category", category);

		return "views/question/question-list";
	}

	@GetMapping("write")
	public ModelAndView insertBoard(ModelAndView mv) {
		ArrayList<Category> categories = bService.selectCategory();

		if (categories != null) {
			mv.addObject("categories", categories).setViewName("question-write");
			return mv;
		} else {
			throw new QnaException("카테고리 목록이 없습니다.");
		}

	}

	@PostMapping("insert")
	public String insertBoard(@ModelAttribute Qna q, HttpSession session, @RequestParam(value="noticeBox", required = false) boolean noticeBox) {
		/* q.setUserNick(session.getId()); */
		q.setWriter(((Member) session.getAttribute("loginUser")).getUserNo());
		
		String notice = noticeBox ? "Y" : "N";
		//System.out.println(notice);

		q.setNotice(notice);
		System.out.println(q);

		int result = bService.insertBoard(q);
		if (result > 0) {
			return "redirect:/qna/list";
		} else {
			throw new QnaException("게시글 작성을 실패하였습니다.");
		}
	}

	// 상세 페이지 글 조회 이동
	@GetMapping("/{qnaNo}/{page}") // 글번호/페이지
	public ModelAndView selectBoard(@PathVariable("qnaNo") int qNo, @PathVariable("page") int page, HttpSession session,
			ModelAndView mv) {

		Member loginUser = (Member) session.getAttribute("loginUser");
		String id = null;
		if (loginUser != null) {
			id = loginUser.getUserNick();
		}

		Qna q = bService.selectBoard(qNo, id);

		ArrayList<reply> r = bService.selectReply(qNo);
		
		if (q != null) {
			mv.addObject("q", q).addObject("page", page).addObject("r", r).setViewName("views/question/question-post");
			return mv;
		} else {
			throw new QnaException("게시글 상세조회를 실패하였습니다.");
		}

	}


	
	@GetMapping("/{qnaNo}/{page}/updatePost")  
	public ModelAndView updatePost(ModelAndView mv, @PathVariable("page") int page, @PathVariable("qnaNo") int qNo
									, HttpSession session) { 
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String id = null;
		if (loginUser != null) {
			id = loginUser.getUserId();
		}
		
		
		ArrayList<Category> categories = bService.selectCategory();
		
		Qna q = bService.selectBoard(qNo, id);
		
		
		System.out.println(page);
		if (categories != null) {
			mv.addObject("page", page).addObject("q", q);
			mv.addObject("categories", categories).setViewName("question-update");
			return mv;
		} else {
			throw new QnaException("카테고리 목록이 없습니다.");
		}
	}
	
//	@PostMapping("updatePost")
//	public String updateBoard(@ModelAttribute Qna q, @ModelAttribute reply r, @RequestParam("page") int page, 
//							  @RequestParam(value="noticeBox", required = false) boolean noticeBox, HttpSession session) {
//		q.setWriter(((Member) session.getAttribute("loginUser")).getUserNo());
//		
//		System.out.println(q);
//		
//		String notice = noticeBox ? "Y" : "N";
//		
//		q.setNotice(notice);
//		
//		int result = bService.updateBoard(q);
//		if (result > 0) {
//			return String.format("redirect:/qna/%d/%d", q.getQnaNo(), page);
//		} else {
//			throw new QnaException("게시글 수정을 실패하였습니다.");
//		}
//	}
	
	@PostMapping("updatePost")
	public String updateBoard(@ModelAttribute Qna q,  @RequestParam("page") int page, 
	                          @RequestParam(value = "noticeBox", required = false) String noticeBox, 
	                          HttpSession session) {
	    
	    // 로그인 유저 확인
	    Member loginUser = (Member) session.getAttribute("loginUser");
	    if (loginUser == null) {
	        throw new QnaException("로그인이 필요합니다.");
	    }
	    q.setWriter(loginUser.getUserNo());
	    
	    System.out.println("수정 전 : " + noticeBox);
	    // 공지 여부 설정 (체크되면 "Y", 아니면 "N")
	    
	    if(noticeBox == null) {
	    	noticeBox = "N";
	    }
	    
	    q.setNotice(noticeBox);
	       
	    // 게시글 업데이트 실행
	    int result = bService.updateBoard(q);
	    if (result > 0) {
	        return String.format("redirect:/qna/%d/%d", q.getQnaNo(), page);
	    } else {
	        throw new QnaException("게시글 수정에 실패하였습니다.");
	    }
	}


	@PostMapping("deletePost")
    public String deletePost(@RequestParam("qnaNo") int qNo) {
        bService.deletePost(qNo);
        return "redirect:/qna/list";
	}
	
	@GetMapping("notice")
	public int noticeBoard(@ModelAttribute Qna q) {
		return bService.noticeBoard(q);
	}
	
	@GetMapping("list/{category}")
	public ModelAndView filter(@PathVariable("category") String category, 
							   @RequestParam(value = "page", defaultValue = "1") int currentPage,
							   @RequestParam(value="condition", required = false) String condition,
							   @RequestParam(value = "search", required = false) String search,
							   ModelAndView mv, HttpServletRequest request) {
		if(condition == null || condition == "-") {
			condition = null;
		}
		
		switch(category) {
		case "NOTICE": category = "Y"; break;
		case "Q&A" : category = "N";	break;
		default : category = null; break;
		}
		System.out.println(condition);
		System.out.println(search);
		
		HashMap<String, String> map = new HashMap<>();
		map.put("category", category);
		map.put("search", search);
		map.put("condition", condition);
		
		// 카테고리에 따른 리스트 카운트 조회
		int listCount = bService.getListCount(map);
		System.out.println("리스트 카운트 : " + listCount);
		
		PageInfo pi = new PageInfo();
		pi = Pagination.getPageInfo(currentPage, listCount, 10);

		
		
		// 조건에 따른 리스트 조회
		ArrayList<Qna> list = bService.selectAllBoardList(map, pi);
		
		System.out.println(list);
		System.out.println(request.getRequestURI());
		
		mv.addObject("search", search).addObject("condition", condition);
		mv.addObject("loc", request.getRequestURI());
		mv.addObject("list", list).addObject("pi", pi).setViewName("views/question/question-list");
		
		
		return mv;
	}
	
	@PostMapping("insertReply")
	public String insertReply(@ModelAttribute Qna q, @ModelAttribute reply r, @RequestParam("page") int page,
			HttpSession session) {
		r.setUserNo(((Member) session.getAttribute("loginUser")).getUserNo());
		r.setQnaNo(q.getQnaNo());
		
		System.out.println(page);
		int result = bService.insertReply(r);
		if (result > 0) {
			return String.format("redirect:/qna/%d/%d", q.getQnaNo(), page);
		} else {
			throw new QnaException("댓글 등록을 실패하셨습니다.");
		}
	}
	
	// jackson 버전
	   @GetMapping(value="rinsert", produces="application/json; charset=UTF-8")
	   @ResponseBody
	   public String insertReply(@ModelAttribute reply r) {
		   // 저장
		   int result = bService.insertReply(r);
		   // 가져오기
		   ArrayList<reply> list = bService.selectReply(r.getRepNo());
		
		   ObjectMapper om = new ObjectMapper();
		   
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   om.setDateFormat(sdf);
		   
		   String strJson = null;
		   try {
			strJson = om.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		   return strJson;
	   }
	   
	   
	   @GetMapping("rdelete")
	   @ResponseBody
	   public int deleteReply(@RequestParam("rId") int rId) {
		   return  bService.deleteReply(rId);
		
	   }
	   
	   @GetMapping("rupdate")
	   @ResponseBody
	   public int updateReply(@ModelAttribute reply r) {
		   System.out.println("수정 요청 받은 댓글: " + r);
		   return bService.updateReply(r);
	   }
	
	
	
	
}