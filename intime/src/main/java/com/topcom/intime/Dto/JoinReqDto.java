package com.topcom.intime.Dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class JoinReqDto {
	@NotEmpty(message = "이메일을 꼭 입력하십시오")
	@Email
	private String email;

	private String password;
}
