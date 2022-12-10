package com.topcom.intime.Controller;

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

import com.topcom.intime.Dto.ForumReqDto;
import com.topcom.intime.Dto.ForumResDto;
import com.topcom.intime.Dto.ResponseDto;
import com.topcom.intime.service.ForumService;

@RestController
public class ForumApiController {

	@Autowired
	private ForumService forumService;
	
	@PostMapping("api/forum")
	public ResponseDto<Integer> postForum (@RequestBody ForumReqDto forumDto) {
		int savedForumId = forumService.saveForum(forumDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), savedForumId);
	} 
	
	@GetMapping("api/myforums")
	public List<ForumResDto> getAllForumsByUser() {
		return forumService.getAllForumsByUser();
	}
	
	@GetMapping("api/forums/all")
	public List<ForumResDto> getAllForums() {
		return forumService.getAllForums();
	}
	
	@PutMapping("api/forum={id}")
	public ResponseDto<Integer> updateForumById (@PathVariable("id")int id, @RequestBody ForumReqDto forumDto) {
		forumService.updateForumById(id, forumDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("api/forum={id}") 
	public ResponseDto<Integer> deleteForumById (@PathVariable("id")int id) {
		forumService.deleteForumById(id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
}
