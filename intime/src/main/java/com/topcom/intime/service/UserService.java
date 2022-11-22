package com.topcom.intime.service;

import com.topcom.intime.Dto.UpdatePasswordReqDto;
import com.topcom.intime.Dto.UpdateUsernameReqDto;
import com.topcom.intime.exception.APIException;
import com.topcom.intime.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	//비밀번호 변경
	public void updatePwd(int useridx, UpdatePasswordReqDto updatePasswordReqDto)  {
		User user=userRepository.findById(useridx).orElseThrow(()-> new ResourceNotFoundException("User", "id", (long)useridx));
		user.setPassword(bCryptPasswordEncoder.encode(updatePasswordReqDto.getNewPwd()));
		userRepository.save(user);
	}
	//유저 닉네임 변경//
	public void updateUsername(int useridx, UpdateUsernameReqDto updateUsernameReqDto){
		User user=userRepository.findById(useridx).orElseThrow(()-> new ResourceNotFoundException("User", "id", (long)useridx));
		if(user.getUsername().equals(updateUsernameReqDto.getUsername())){
			throw new APIException(HttpStatus.BAD_REQUEST, "변경 전과 동일한 닉네임입니다.");
		}
		if(userRepository.existsByUsername(updateUsernameReqDto.getUsername())){
			throw new APIException(HttpStatus.BAD_REQUEST, "해당 닉네임이 이미 사용중입니다.");
		}
		user.setUsername(updateUsernameReqDto.getUsername());
		userRepository.save(user);
	}
}
