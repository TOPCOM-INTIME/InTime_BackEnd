package com.topcom.intime.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.topcom.intime.Dto.LoginReqDto;
import com.topcom.intime.Dto.ResponseDto;
import com.topcom.intime.model.User;
import com.topcom.intime.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	@Autowired UserService userService;

	@PostMapping("join")
	public ResponseDto<Integer> join(@RequestBody LoginReqDto loginReqDto) {
		User user = userService.findUser(loginReqDto.getEmail());
		if (userService.findUser(loginReqDto.getEmail()) != null) {
			return new ResponseDto<Integer>(HttpStatus.CONFLICT.value(), -1);
		}
		userService.Join(loginReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@GetMapping("/api/user")
	public String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	
}
