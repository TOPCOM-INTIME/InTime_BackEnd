package com.topcom.intime.Controller;

import java.util.List;

import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.UserRepository;
import com.topcom.intime.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.topcom.intime.Dto.ReadyPatternGroupReqDto;
import com.topcom.intime.Dto.ReadyPatternGroupResDto;
import com.topcom.intime.Dto.ReadyPatternReqDto;
import com.topcom.intime.Dto.ReadyPatternResDto;
import com.topcom.intime.Dto.ResponseDto;
import com.topcom.intime.model.ReadyPattern;
import com.topcom.intime.model.ReadyPatternGroup;
import com.topcom.intime.repository.ReadyPatternGroupRepository;
import com.topcom.intime.repository.ReadyPatternRepository;
import com.topcom.intime.service.ReadyPatternGroupService;
import com.topcom.intime.service.ReadyPatternService;

@RestController
public class TestApiController {

	@Autowired
	private ReadyPatternGroupRepository readyPatternGroupRepository;
	
	@Autowired
	private ReadyPatternRepository readyPatternRepository;

	private UserRepository userRepository;

	private final EmailService emailService;

	public TestApiController(UserRepository userRepository, EmailService emailService){
		this.userRepository=userRepository;
		this.emailService=emailService;
	}
	@GetMapping("/test/{id}")
	public void test(@PathVariable("id")int id) {
		List<ReadyPattern> list = readyPatternGroupRepository.test(id);
		System.out.println(list);
	}

	@GetMapping("/email") //UserApiController로 보낼 예정
	public ResponseEntity<String> sendEmail(@RequestParam String email){
		try{
			User user=userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "email", Long.parseLong(email)));
			String code=user.getPassword();
			emailService.sendMessage(email, code);
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>("이메일 전송 실패", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("이메일 전송 성공", HttpStatus.OK);
	}
	
}
