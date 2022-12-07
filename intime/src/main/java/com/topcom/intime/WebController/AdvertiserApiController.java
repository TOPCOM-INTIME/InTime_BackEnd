package com.topcom.intime.WebController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.topcom.intime.service.NoticeService;
import com.topcom.intime.service.UserService;

@RestController
public class AdvertiserApiController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private NoticeService noticeService;

	
}
