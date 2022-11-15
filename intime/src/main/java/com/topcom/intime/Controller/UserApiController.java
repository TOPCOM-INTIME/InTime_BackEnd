package com.topcom.intime.Controller;

import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.repository.UserRepository;
import com.topcom.intime.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.topcom.intime.Dto.LoginReqDto;
import com.topcom.intime.Dto.ResponseDto;
import com.topcom.intime.model.User;
import com.topcom.intime.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	@Autowired private UserService userService;

	@Autowired
	private UserRepository userRepository;


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
