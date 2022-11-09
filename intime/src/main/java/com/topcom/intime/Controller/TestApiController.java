package com.topcom.intime.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
	
	@GetMapping("/test/{id}")
	public void test(@PathVariable("id")int id) {
		List<ReadyPattern> list = readyPatternGroupRepository.test(id);
		System.out.println(list);
	}
	
	
}
