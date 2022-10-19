package com.topcom.intime.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topcom.intime.model.User;
import com.topcom.intime.repository.UserRepository;
import com.topcom.intime.service.UserService;

@RestController
public class HttpTestController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/http/get")
	public User getTest(User user) {
		User finded_user = userService.findUser(user.getUsername());
		return finded_user;
	}

	@PostMapping("/http/post")
	public String postTest(User user) {
		
		userRepository.save(user);
		return "post요청";
	}

	@PutMapping("/http/put/{id}")
	public String putTest(@PathVariable int id) {
		return "put요청 성공" + id;
	}

	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete요청";
	}
}
