package com.sinuedu.user.member.controller;

import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.sinuedu.user.member.exception.MemberException;
import com.sinuedu.user.member.model.service.MemberService;
import com.sinuedu.user.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
@RequestMapping("/member/")
public class MemberController {
	
	private final MemberService mService;
	
	private final BCryptPasswordEncoder bcrypt;    // 복호화 불가능하게 만드는 역할


	@GetMapping("loginPage")
	public String loginPage() {
		return "views/member/loginPage";
	}
	// 로그인
	@PostMapping("login")
	public String login(Member m, HttpSession session, HttpServletRequest requset) {
		System.out.println(bcrypt.encode("admin"));
		Member loginUser = mService.login(m);
		if(loginUser != null && bcrypt.matches(m.getUserPw(), loginUser.getUserPw())) {
			session.setAttribute("loginUser", loginUser);
			System.out.println(loginUser);
			return "redirect:/home";
		} else {
			throw new MemberException("로그인을 실패하였습니다");
		}
	}
	
	// 로그아웃
	@GetMapping("logout")
	public String login(SessionStatus session) {
		session.setComplete();
		return "redirect:/home";
	}
	
	// 메뉴바 -> 회원가입
	@GetMapping("join")
	public String join1() {
		return "join1";
	}
	
	// 회원가입 1페이지
	@PostMapping("join2")
	public String join2(@ModelAttribute Member m, Model model) {
		model.addAttribute("member", m);
		return "join2";
	}
	
	// 아이디 중복체크
		@GetMapping("checkId")
		public void checkId(@RequestParam("userId") String userId, PrintWriter out) {
			int count = mService.checkId(userId);
			out.print(count);
		}
	
	// 회원가입 2페이지
		@PostMapping("join3")
		public String join3(
		        @ModelAttribute Member m,
		        @RequestParam("phone1") String phone1,
		        @RequestParam("phone2") String phone2,
		        @RequestParam("phone3") String phone3,
		        @RequestParam("birth1") String birth1,
		        @RequestParam("birth2") String birth2,
		        @RequestParam("birth3") String birth3) throws ParseException {
			
			if (phone1 == null || phone1.isEmpty() ||
		        phone2 == null || phone2.isEmpty() ||
		        phone3 == null || phone3.isEmpty() ||
		        birth1 == null || birth1.isEmpty() ||
		        birth2 == null || birth2.isEmpty() ||
		        birth3 == null || birth3.isEmpty()) {
		        throw new MemberException("회원가입을 실패하였습니다");
		    }

		    // 1. 사용자 정보 처리
		    m.setPhone(phone1 + "-" + phone2 + "-" + phone3);

		    String Birth = birth1 + "-" + birth2 + "-" + birth3;
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		    java.util.Date utilDate = formatter.parse(Birth);

		    Date date = new Date(utilDate.getTime());
		    m.setBirthDate(date);
		    m.setUserPw(bcrypt.encode(m.getUserPw()));

		    // 2. 회원가입 처리
		    int result = mService.insertMember(m);

		    // 3. 처리 결과 반환
		    if (result > 0) {
		        return "join3"; // 성공 페이지로 이동
		    } else {
		        throw new MemberException("회원가입을 실패하였습니다");
		    }
		}
	
	// 닉네임 중복체크
	@GetMapping("checkUserNick")
	public void checkUserNick(@RequestParam("userNick") String userNick, PrintWriter out) {
		int count = mService.checkUserNick(userNick);
		out.print(count);
	}
	
	// 메뉴바 -> 아이디 찾기
	@GetMapping("find-id")
	public String findId() {
		return "find-id";
	}
	
	// 아이디 찾기 페이지
	@PostMapping("find-id")
	public String findMyId(@ModelAttribute Member m,
						   @RequestParam("phone1") String phone1, 
						   @RequestParam("phone2") String phone2, 
						   @RequestParam("phone3") String phone3, 
						   @RequestParam("birth1") String birth1,
						   @RequestParam("birth2") String birth2, 
						   @RequestParam("birth3") String birth3,
						   Model model) throws ParseException {
		
		if (phone1 == null || phone1.isEmpty() ||
	        phone2 == null || phone2.isEmpty() ||
	        phone3 == null || phone3.isEmpty() ||
	        birth1 == null || birth1.isEmpty() ||
	        birth2 == null || birth2.isEmpty() ||
	        birth3 == null || birth3.isEmpty()) {
	        return "find-id-error"; // 입력값 중 하나라도 비어 있으면 에러 페이지로 이동
	    }
	    
		String phone = phone1 + "-" + phone2 + "-" + phone3;
		String birth = birth1 + "-" + birth2+ "-" +birth3;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = formatter.parse(birth);
        Date date = new Date(utilDate.getTime());
        
        m.setBirthDate(date);
        m.setPhone(phone);
        
        List<String> userIds = mService.findMyId(m);
			
		if(userIds.isEmpty()) {
			return "find-id-error";
		} else {
			model.addAttribute("userIds", userIds);
			return "find-id-success";
		}
	}
	
	// 메뉴바 -> 비밀번호 찾기
	@GetMapping("find-pwd")
	public String findPwd() {
		return "find-pwd";
	}
	
	// 비밀번호 찾기 페이지
	@PostMapping("find-pwd")
	public String findPassword(
	        @RequestParam("userId") String userId,
	        @RequestParam("hint") String hint,
	        @RequestParam("hintAnswer") String hintAnswer,
	        Model model) {

	    try {
	        // 1. 사용자 정보 확인
	        Map<String, Object> params = new HashMap<>();
	        params.put("userId", userId);
	        params.put("hint", hint);
	        params.put("hintAnswer", hintAnswer);

	        Member foundMember = mService.findMyPw(params);

	        if (foundMember == null) {
	            // 사용자가 입력한 정보가 DB와 일치하지 않을 경우 에러 페이지로 이동
	            return "find-pwd-error";
	        }

	        // 2. 임시 비밀번호 생성
	        String tempPassword = generateTemporaryPassword();

	        // 3. 임시 비밀번호 암호화 및 DB 업데이트
	        String encodedPassword = bcrypt.encode(tempPassword);
	        mService.updatePw(foundMember.getUserId(), encodedPassword);

	        // 4. 사용자에게 보여줄 임시 비밀번호 설정
	        model.addAttribute("tempPassword", tempPassword);

	        // 성공 페이지로 이동
	        return "find-pwd-success";

	    } catch (Exception e) {
	        // 예외 발생 시 에러 페이지로 이동
	        e.printStackTrace();
	        return "find-pwd-error";
	    }
	}

	// 임시 비밀번호 생성 메서드
	private String generateTemporaryPassword() {
	    return UUID.randomUUID().toString().substring(0, 8); // 8자리 랜덤 문자열 생성
	}

	
	// 메뉴바 -> 마이페이지
	@GetMapping("my-page")
	public String edit(HttpSession session) {
		System.out.println((Member)session.getAttribute("loginUser"));
		return "my-page";
	}
	
	// 마이페이지
	@PostMapping("my-page")
	public String editMyInfo(@ModelAttribute Member m,
							 @RequestParam("newUserPw") String newPw, 
						   	 @RequestParam("phone1") String phone1, 
							 @RequestParam("phone2") String phone2, 
							 @RequestParam("phone3") String phone3, 
							 @RequestParam("birth1") String birth1,
							 @RequestParam("birth2") String birth2, 
							 @RequestParam("birth3") String birth3, HttpSession session, Model model) throws ParseException {
		
		Member loginUser =(Member)session.getAttribute("loginUser");
		
		String Phone = phone1 + "-" + phone2 + "-" + phone3;
		m.setPhone(Phone);
		
		String Birth = birth1 + "-" + birth2 + "-" + birth3;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = formatter.parse(Birth);
        
        Date date = new Date(utilDate.getTime());
        
        m.setBirthDate(date);
        m.setUserId(loginUser.getUserId());
        
        
        if (!newPw.isBlank()) {
            m.setUserPw(bcrypt.encode(newPw));
        } else {
            m.setUserPw(loginUser.getUserPw());
        }

        // 비밀번호 암호화 및 업데이트
        HashMap<String, String> map = new HashMap<>();
        map.put("id", m.getUserId());
        map.put("newUserPw", bcrypt.encode(newPw));

		int result = mService.updateMember(m);
		System.out.println("result : " + result);
		
		Member updatedUser = mService.login(m);
		
		if(result > 0) {
			model.addAttribute("loginUser", updatedUser);
			return "redirect:/";
		} else {
			throw new MemberException("회원정보 수정을 실패하였습니다");
		}
	}
	
	
}


