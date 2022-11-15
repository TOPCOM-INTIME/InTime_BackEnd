package com.topcom.intime.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RestController;

import com.topcom.intime.model.ReadyPattern;
import com.topcom.intime.repository.ReadyPatternGroupRepository;

@RestController
public class TestApiController {

	@Autowired
	private ReadyPatternGroupRepository readyPatternGroupRepository;
	
	@GetMapping("/test/{id}")
	public void test(@PathVariable("id")int id) {
		List<ReadyPattern> list = readyPatternGroupRepository.test(id);
		System.out.println(list);
	}
	
	
}
