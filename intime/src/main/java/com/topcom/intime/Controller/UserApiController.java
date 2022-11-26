package com.topcom.intime.Controller;

import com.topcom.intime.Dto.*;
import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.repository.UserRepository;
import com.topcom.intime.service.EmailService;
import com.topcom.intime.service.FriendService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.topcom.intime.auth.PrincipalDetails;
import com.topcom.intime.model.User;
import com.topcom.intime.service.UserService;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	@Autowired private UserService userService;
	@Autowired private FriendService friendService;

	@PostMapping("join")
	public ResponseDto<Integer> join(@Valid @RequestBody JoinReqDto joinReqDto) {
		if (userService.findUser(joinReqDto.getEmail()) != null) {
			return new ResponseDto<Integer>(HttpStatus.CONFLICT.value(), -1);
		}
		userService.Join(joinReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/device-token")
	public ResponseDto<Integer> update_deviceToken(@RequestBody DeviceTokenDto tokenDto) {
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		PrincipalDetails principal = (PrincipalDetails)principalObject;
		
		userService.update_deviceToken(principal.getUser().getId(), tokenDto.getDeviceToken());
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	
	@GetMapping("/api/user")
	public String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}

	@ApiOperation(value="Change user's password")
	@PutMapping("/password")
	public ResponseEntity<String> updatePwd(@RequestBody UpdatePasswordReqDto updatePasswordReqDto){
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails)principalObject;

		userService.updatePwd(principal.getUser().getId(), updatePasswordReqDto);
		return new ResponseEntity<>("비밀번호 변경 성공", HttpStatus.OK);
	}

	@ApiOperation(value="Change user's name")
	@PutMapping("/username")
	public ResponseEntity<String> updateUsername(@RequestBody UpdateUsernameReqDto updateUsernameReqDto){
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails)principalObject;

		userService.updateUsername(principal.getUser().getId(), updateUsernameReqDto);
		return new ResponseEntity<>("닉네임 변경 성공", HttpStatus.OK);
	}

	@ApiOperation(value="getting individual's friends")
	@GetMapping("/friends")
	public List<FriendResDto> getAllFriends(){
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails)principalObject;

		return friendService.getAllFriends(principal.getUser().getId());
	}

	@ApiOperation(value="Deleting individual's friends")
	@DeleteMapping("/friends")
	public ResponseEntity<String> deleteFriends(@RequestBody DeleteFriendsReqDto deleteFriendsReqDto){
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails)principalObject;

		friendService.deleteFriends(principal.getUser().getId(), deleteFriendsReqDto);
		return new ResponseEntity<>("친구 목록에서 삭제되었습니다.", HttpStatus.OK);
	}

	@ApiOperation(value="requesting for being a friend")
	@PostMapping("/friends/request")
	public ResponseEntity<String> request(@RequestBody FriendsReqDto friendsReqDto){
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails)principalObject;

		friendService.addFriends(principal.getUser().getId(), friendsReqDto);
		return new ResponseEntity<>("친구 요청 성공", HttpStatus.OK);
	}

	@ApiOperation(value="accepting the request for being a friend")
	@PutMapping("/friends/request/{friendsIdx}")
	public ResponseEntity<String> acceptRequest(@PathVariable int friendsIdx){
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails)principalObject;

		friendService.accept(principal.getUser().getId(), friendsIdx);
		return new ResponseEntity<>("친구 요청 수락", HttpStatus.OK);
	}

	@ApiOperation(value="Denying the request for being a friend")
	@DeleteMapping("/friends/request/{friendsIdx}")
	public ResponseEntity<String> denyRequest(@PathVariable int friendsIdx){
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails)principalObject;

		friendService.deny(principal.getUser().getId(), friendsIdx);
		return new ResponseEntity<>("친구 요청 거절", HttpStatus.OK);
	}

	@ApiOperation(value="getting all the requests for being a friend")
	@GetMapping("/friends/request")
	public List<FriendsReqListDto> getAllRequests(){
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails)principalObject;

		return friendService.getAllRequests(principal.getUser().getId());
	}
}
