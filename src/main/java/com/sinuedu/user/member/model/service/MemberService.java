package com.sinuedu.user.member.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.binding.MapperRegistry;
import org.springframework.stereotype.Service;

import com.sinuedu.user.member.model.mapper.MemberMapper;
import com.sinuedu.user.member.model.vo.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberMapper mapper;

	public Member login(Member m) {
		return mapper.login(m);
	}

	public int insertMember(Member m) {
		return mapper.insertMember(m);
	}

	public int checkId(String userId) {
		return mapper.checkId(userId);
	}

	public int checkUserNick(String userNick) {
		return mapper.checkUserNick(userNick);
	}
	public List<String> findMyId(Member m) {
		return mapper.findMyId(m);
	}

	public int updateMember(Member m) {
		return mapper.updateMember(m);
	}

	public int updatePw(String userId, String encodedPassword) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("userId", userId);
	    params.put("encodedPassword", encodedPassword);
	    return mapper.updatePw(params);
	}

	public Member findMyPw(Map<String, Object> params) {
		return mapper.findMyPw(params);
	}

}
