package com.sinuedu.user.manager.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sinuedu.board.lecture.model.service.LectureService;
import com.sinuedu.board.lecture.model.vo.Category;
import com.sinuedu.board.lecture.model.vo.Chapter;
import com.sinuedu.board.lecture.model.vo.Image;
import com.sinuedu.board.lecture.model.vo.Lecture;
import com.sinuedu.user.manager.exception.ManagerException;
import com.sinuedu.user.manager.model.service.ManagerService;
import com.sinuedu.user.member.model.vo.Member;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
@RequestMapping("/manager")
public class ManagerController {

	private final ManagerService mService;
	private final LectureService lService;

	@GetMapping("/userList")
	public String userList(Model model) {
		List<Member> list = mService.userList();
		model.addAttribute("list", list);
		return "userList";
	}

	@GetMapping("/chapterList")
	public String chapterList(Model model) {
		List<Chapter> list = mService.chapterList();
		System.out.println(list);
		Set<String> categories = list.stream().map(Chapter::getCgName).collect(Collectors.toSet());
		model.addAttribute("list", list);
		model.addAttribute("categories", categories);
		return "chapterList";
	}

	@GetMapping("/lectureAdd")
	public String lectureAddPage(Model model) {
		List<Category> categories = mService.categoryList();
		model.addAttribute("categories", categories);
		return "lectureAdd";
	}
	
	@PostMapping("lectureInsert")
	public String lectureInsert(@ModelAttribute Lecture lec, @RequestParam("file") MultipartFile file) {
		Image i = new Image();
		if(!file.getOriginalFilename().equals("")) {
			String[] returnArr = saveFile(file);
			
			i.setImgName(file.getOriginalFilename());
			i.setImgRename(returnArr[1]);
			i.setImgPath(returnArr[0]);
		}
		
		System.out.println(lec);
		System.out.println(i);
		
		int result1 = 1;
		int result2 = 1;
		
		result1 = mService.insertLecture(lec);
		
		System.out.println("insert 후 : "+lec);
		
		i.setRefLecNo(lec.getLecNo());
		
		result2 = mService.insertImage(i);
		
		if(result1 + result2 == 2) {
			return "redirect:/manager/chapterList";
		}else {
			deleteFile(i.getImgRename());
			throw new ManagerException("오류 발생");
		}
		
	}
	
	@PostMapping("lectureUpdate")
	public String lectureUpdate(@ModelAttribute Lecture lec, @RequestParam("file") MultipartFile file) {
		Image i = new Image();
		
		if(!file.getOriginalFilename().equals("")) {
			String[] returnArr = saveFile(file);
			
			i.setImgName(file.getOriginalFilename());
			i.setImgRename(returnArr[1]);
			i.setImgPath(returnArr[0]);
		}
		
		System.out.println(lec);
		System.out.println(i);
		
		int result1 = 1;
		int result2 = 1;
		
		result1 = mService.updateLecture(lec);
		i.setRefLecNo(lec.getLecNo());
		result2 = mService.updateImage(i);
		
		if(result1 + result2 == 2) {
			return "redirect:/lecture/" + lec.getLecNo();
		}else {
			deleteFile(i.getImgRename());
			throw new ManagerException("오류 발생");
		}
		
	}

	private void deleteFile(String imgRename) {
		String SavePath = "d:\\dev\\uploadFiles";
		
		File f = new File(SavePath + "\\" + imgRename);
		if(f.exists()) {
			f.delete();
		}
	}

	private String[] saveFile(MultipartFile file) {
		String savePath = "d:\\dev\\uploadFile";
		File folder = new File(savePath);
		if(!folder.exists()) {
			folder.mkdir();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		int ranNum = (int)(Math.random()*100000);
		String originFileName = file.getOriginalFilename();
		String renameFileName = sdf.format(new Date()) + ranNum + originFileName.substring(originFileName.lastIndexOf("."));
		
		String renamePath = folder + "\\" + renameFileName;
		try {
			file.transferTo(new File(renamePath));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		
		String[] returnArr = new String[2];
		returnArr[0] = savePath;
		returnArr[1] = renameFileName;
		
		return returnArr;
	}

	@GetMapping("/chapterAdd")
	public String chapterAdd(Model model) {
		List<Chapter> chapters = mService.chapterList();
		List<Lecture> lectures = mService.lectureList();
		Set<String> categories = chapters.stream().map(Chapter::getCgName).collect(Collectors.toSet());
		model.addAttribute("categories", categories);
		model.addAttribute("lectures", lectures);
		return "chapterAdd";
	}

	@PostMapping("/deleteChapter")
	@ResponseBody
	public int deleteChapter(@RequestParam("chapNo") int chapNo) {
		mService.deleteViewChapter(chapNo);
		return mService.deleteChapter(chapNo);
	}

	@PostMapping("/insertChapter")
	@ResponseBody
	public Map<String, Object> insertChapter(@ModelAttribute Chapter chapter) {
		Map<String, Object> response = new HashMap<>();
		try {
			int result = mService.insertChapter(chapter);
			if (result > 0) {
				response.put("success", true);
			} else {
				response.put("success", false);
				response.put("message", "챕터 추가에 실패했습니다.");
			}
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "서버 오류: " + e.getMessage());
		}
		return response;
	}

	@GetMapping("/chapterEdit")
	public String chapterEdit(@RequestParam("chapNo") int chapNo, Model model) {
		Chapter chapter = mService.getChapterByTitle(chapNo);
		List<Chapter> chapters = mService.chapterList();
		List<Lecture> lectures = mService.lectureList();
		Set<String> categories = chapters.stream().map(Chapter::getCgName).collect(Collectors.toSet());

		model.addAttribute("chapter", chapter);
		model.addAttribute("categories", categories);
		model.addAttribute("lectures", lectures);
		return "chapterEdit";
	}

	@PostMapping("/updateChapter")
	@ResponseBody
	public Map<String, Object> updateChapter(@ModelAttribute Chapter chapter) {
		Map<String, Object> response = new HashMap<>();
		try {
			int result = mService.updateChapter(chapter);
			response.put("success", result > 0);
			response.put("message", result > 0 ? "성공적으로 수정되었습니다." : "수정에 실패했습니다.");
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "서버 오류: " + e.getMessage());
		}
		return response;
	}
	
	@PostMapping("/toggleAdmin")
	@ResponseBody
	public Map<String, Object> toggleAdmin(@RequestParam("userId") String userId, @RequestParam("adminStatus") String adminStatus) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        int result = mService.updateAdminStatus(userId, adminStatus);
	        response.put("success", result > 0);
	    } catch (Exception e) {
	        response.put("success", false);
	    }
	    return response;
	}

	@PostMapping("/toggleStatus")
	@ResponseBody
	public Map<String, Object> toggleStatus(@RequestParam("userId") String userId, @RequestParam("status") String status) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        int result = mService.updateUserStatus(userId, status);
	        response.put("success", result > 0);
	    } catch (Exception e) {
	        response.put("success", false);
	    }
	    return response;
	}
	
	@GetMapping("lectureDelete/{lNo}/{imgRename}")
	public String lectureDelete(@PathVariable("lNo") int lecNo, @PathVariable("imgRename") String imgRename) {
		mService.deleteAllChapter(lecNo);
		System.out.println(imgRename);
		deleteFile(imgRename);
		int result = mService.deleteLecture(lecNo);
		
		if(result >0) {
			return "redirect:/lecture/list";
		}else {
			throw new ManagerException("삭제 중 오류가 발생했습니다");
		}
	}
	
	@GetMapping("lectureUpdate/{lNo}")
	public ModelAndView lectureUpdate(@PathVariable("lNo") int lecNo, ModelAndView mv) {
		ArrayList<Lecture> lList = lService.selectLectureList(lecNo);
		Lecture lecture = lList.get(0);
		List<Category> categories = mService.categoryList();
		System.out.println(lecture);
		mv.addObject("categories", categories).addObject("lec", lecture).setViewName("lectureEdit");
		return mv;
	}
}