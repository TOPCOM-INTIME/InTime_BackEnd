package com.topcom.intime.Controller;

import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.repository.UserRepository;
import com.topcom.intime.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.topcom.intime.Dto.DeviceTokenDto;
import com.topcom.intime.Dto.JoinReqDto;
import com.topcom.intime.Dto.ResponseDto;
import com.topcom.intime.auth.PrincipalDetails;
import com.topcom.intime.model.User;
import com.topcom.intime.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	@Autowired private UserService userService;

	@PostMapping("join")
	public ResponseDto<Integer> join(@RequestBody JoinReqDto joinReqDto) {
		if (userService.findUser(joinReqDto.getEmail()) != null) {
			return new ResponseDto<Integer>(HttpStatus.CONFLICT.value(), -1);
		}
		userService.Join(joinReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/device-token")
	public ResponseDto<Integer> update_deviceToken(@RequestBody DeviceTokenDto tokenDto) {
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		PrincipalDetails principal = (PrincipalDetails)principalObject;
		
		userService.update_deviceToken(principal.getUser().getId(), tokenDto.getDeviceToken());
		
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
