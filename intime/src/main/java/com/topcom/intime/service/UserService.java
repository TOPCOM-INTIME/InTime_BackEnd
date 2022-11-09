package com.topcom.intime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.LoginReqDto;
import com.topcom.intime.Dto.ResponseDto;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public boolean Join(LoginReqDto loginReqDto) {
		User user = new User();
		user.setEmail(loginReqDto.getEmail());
		user.setPassword(bCryptPasswordEncoder.encode(loginReqDto.getPassword()));
		user.setRoles("ROLE_USER");
		userRepository.save(user);
		return true;
	}
	
	@Transactional
	public User findUser(String email) {

		User user = userRepository.findByEmail(email)
				.orElseGet(()->{
					return null;
				});
		return user;
	}
	
	
	
}
