package com.sinuedu.user.member.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sinuedu.user.member.model.vo.Member;

@Mapper
public interface MemberMapper {

	Member login(Member m);

	int insertMember(Member m);

	int checkId(String userId);
	
	int checkUserNick(String userNick);

	List<String> findMyId(Member m);

	int updateMember(Member m);

	int updatePw(String userId, String encodedPassword);

	int updatePw(Map<String, Object> params);

	Member findMyPw(Map<String, Object> params);


}
