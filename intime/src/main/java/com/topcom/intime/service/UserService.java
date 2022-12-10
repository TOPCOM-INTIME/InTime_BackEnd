package com.topcom.intime.service;

import com.topcom.intime.Dto.UpdatePasswordReqDto;
import com.topcom.intime.Dto.UpdateUsernameReqDto;
import com.topcom.intime.Dto.UserResDto;
import com.topcom.intime.WebController.Dto.webJoinDto;
import com.topcom.intime.exception.APIException;
import com.topcom.intime.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.JoinReqDto;
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
		user.setUsername(joinReqDto.getUsername());
		user.setRoles("ROLE_USER");
		userRepository.save(user);
		return true;
	}
	
	@Transactional
	public boolean adminJoin(webJoinDto joinDto) {
		User user = new User();
		user.setEmail(joinDto.getEmail());
		user.setPassword(bCryptPasswordEncoder.encode(joinDto.getPassword()));
		user.setUsername(joinDto.getUsername());
		if(joinDto.getType().equals("admin")) {
			user.setRoles("ROLE_ADMIN");
		} else {
			user.setRoles("ROLE_ADVERTISER");
		}
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
	public Integer plusLateCount(int uid) {
		User user = userRepository.findById(uid)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find user By Id : " + uid);
				});
		Integer lateCount = user.getLateCount();
		if(lateCount == null) {
			lateCount = 1;
			user.setLateCount(lateCount);
		} else {
			user.setLateCount(++lateCount);
		}
		return lateCount;
	}
	
	@Transactional
	public List<UserResDto> getAllUsers() {
		List<UserResDto> userDtoList = new ArrayList<>();
		List<User> users = userRepository.mfindAllUsersByType("ROLE_USER");
		for(User user : users) {
			UserResDto userDto = UserResDto.builder()
					.id(user.getId())
					.email(user.getEmail())
					.username(user.getUsername())
					.build();
			userDtoList.add(userDto);
		}
		return userDtoList;
	}
	
	@Transactional
	public void update_deviceToken(int uid, String token) {
		
		User user = userRepository.findById(uid)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find User by id: " + uid);
				});
		user.setDeviceToken(token);
	}
	//비밀번호 변경
	public void updatePwd(int useridx, UpdatePasswordReqDto updatePasswordReqDto)  {
		User user=userRepository.findById(useridx).orElseThrow(()-> new ResourceNotFoundException("User", "id", (long)useridx));
		user.setPassword(bCryptPasswordEncoder.encode(updatePasswordReqDto.getNewPwd()));
		userRepository.save(user);
	}
	//유저 닉네임 변경//
	public void updateUsername(int useridx, UpdateUsernameReqDto updateUsernameReqDto){
		User user=userRepository.findById(useridx).orElseThrow(()-> new ResourceNotFoundException("User", "id", (long)useridx));
		if(user.getUsername()!=null&&user.getUsername().equals(updateUsernameReqDto.getUsername())){
			throw new APIException(HttpStatus.BAD_REQUEST, "변경 전과 동일한 닉네임입니다.");
		}
		if(userRepository.existsByUsername(updateUsernameReqDto.getUsername())){
			throw new APIException(HttpStatus.BAD_REQUEST, "해당 닉네임이 이미 사용중입니다.");
		}
		user.setUsername(updateUsernameReqDto.getUsername());
		userRepository.save(user);
	}
}
