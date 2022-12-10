package com.topcom.intime.WebController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.topcom.intime.Dto.NoticeReqDto;
import com.topcom.intime.Dto.NoticeResDto;
import com.topcom.intime.Dto.ResponseDto;
import com.topcom.intime.Dto.UserResDto;
import com.topcom.intime.WebController.Dto.webJoinDto;
import com.topcom.intime.service.NoticeService;
import com.topcom.intime.service.UserService;

@RestController
public class AdminApiController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private NoticeService noticeService;

	@PostMapping("/join-admin")
	public ResponseDto<Integer> join(@RequestBody webJoinDto joinDto) {
		
		if (userService.findUser(joinDto.getEmail()) != null) {
			return new ResponseDto<Integer>(HttpStatus.CONFLICT.value(), -1);
		}
		userService.adminJoin(joinDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PostMapping("admin/notice")
	public ResponseDto<Integer> postNotice (@RequestBody NoticeReqDto noticeDto) {
		noticeService.saveNotice(noticeDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	} 
	
	@GetMapping("admin/users")
	public List<UserResDto> getAllUsers() {
		return userService.getAllUsers();
	}
	
	@GetMapping("api/notices")
	public List<NoticeResDto> getAllNotices() {
		return noticeService.getAllNotices();
	}
	
	@PutMapping("admin/notice={id}")
	public ResponseDto<Integer> updateNoticeById (@PathVariable("id")int id, @RequestBody NoticeReqDto noticeDto) {
		noticeService.updateNoticeById(id, noticeDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("admin/notice={id}") 
	public ResponseDto<Integer> deleteNoticeById (@PathVariable("id")int id) {
		noticeService.deleteNoticeById(id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
}
