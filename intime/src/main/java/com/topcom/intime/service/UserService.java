package com.topcom.intime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.JoinReqDto;
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
	public boolean Join(JoinReqDto joinReqDto) {
		User user = new User();
		user.setEmail(joinReqDto.getEmail());
		user.setPassword(bCryptPasswordEncoder.encode(joinReqDto.getPassword()));
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
	
	@Transactional
	public void update_deviceToken(int uid, String token) {
		
		User user = userRepository.findById(uid)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find User by id: " + uid);
				});
		user.setDeviceToken(token);
	}
	
	
}
