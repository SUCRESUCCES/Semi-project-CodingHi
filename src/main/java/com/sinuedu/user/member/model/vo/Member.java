package com.sinuedu.user.member.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {
	private int userNo;
	private String userId;
	private String userPw;
	private String userName;
	private String userNick;
	private String phone;
	private Date birthDate;
	private Date joinDate;
	private String hint;
	private String hintAnswer;
	private String admin;
	private String status;
}
