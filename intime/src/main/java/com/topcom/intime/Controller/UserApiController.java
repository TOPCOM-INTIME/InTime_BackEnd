package com.topcom.intime.Controller;

import com.topcom.intime.Dto.*;
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

	@GetMapping("/api/my-info")
	public UserResDto getMyInfo() {
		User user = getPrincipal();
		UserResDto resDto = UserResDto.builder()
				.id(user.getId())
				.email(user.getEmail())
				.username(user.getUsername())
				.lateCount(user.getLateCount())
				.build();
		return resDto;
	}
	
	@PostMapping("join")
	public ResponseDto<Integer> join(@Valid @RequestBody JoinReqDto joinReqDto) {
		if (userService.findUser(joinReqDto.getEmail()) != null) {
			return new ResponseDto<Integer>(HttpStatus.CONFLICT.value(), -1);
		}
		userService.Join(joinReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PostMapping("/api/late")
	public ResponseDto<Integer> plusLateCount() {
		Integer lateCount = userService.plusLateCount(getPrincipal().getId());
		return new ResponseDto<Integer>(HttpStatus.OK.value(), lateCount);
	}
	
	@PutMapping("/api/device-token")
	public ResponseDto<Integer> update_deviceToken(@RequestBody DeviceTokenDto tokenDto) {
		
		userService.update_deviceToken(getPrincipal().getId(), tokenDto.getDeviceToken());
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	@ApiOperation(value="Change user's password")
	@PutMapping("/password")
	public ResponseEntity<String> updatePwd(@RequestBody UpdatePasswordReqDto updatePasswordReqDto){

		userService.updatePwd(getPrincipal().getId(), updatePasswordReqDto);
		return new ResponseEntity<>("비밀번호 변경 성공", HttpStatus.OK);
	}

	@ApiOperation(value="Change user's name")
	@PutMapping("/username")
	public ResponseEntity<String> updateUsername(@RequestBody UpdateUsernameReqDto updateUsernameReqDto){

		userService.updateUsername(getPrincipal().getId(), updateUsernameReqDto);
		return new ResponseEntity<>("닉네임 변경 성공", HttpStatus.OK);
	}

	@ApiOperation(value="searching nickname by texting")
	@PostMapping("/user")
	public List<FriendsReqDto> searchFriends(@RequestBody FriendsReqDto friendsReqDto){
	
		return friendService.searchFriends(getPrincipal().getId(), friendsReqDto);
	}

	@ApiOperation(value="getting individual's friends")
	@GetMapping("/friends")
	public List<FriendResDto> getAllFriends(){
		return friendService.getAllFriends(getPrincipal().getId());
	}

	@ApiOperation(value="Deleting individual's friends")
	@DeleteMapping("/friends")
	public ResponseEntity<String> deleteFriends(@RequestBody DeleteFriendsReqDto deleteFriendsReqDto){
		friendService.deleteFriends(getPrincipal().getId(), deleteFriendsReqDto);
		return new ResponseEntity<>("친구 목록에서 삭제되었습니다.", HttpStatus.OK);
	}

	@ApiOperation(value="requesting for being a friend")
	@PostMapping("/friends/request")
	public ResponseEntity<String> request(@RequestParam String username){
		friendService.addFriends(getPrincipal().getId(), username);
		return new ResponseEntity<>("친구 요청 성공", HttpStatus.OK);
	}

	@ApiOperation(value="accepting the request for being a friend")
	@PutMapping("/friends/request/{friendsIdx}")
	public ResponseEntity<String> acceptRequest(@PathVariable int friendsIdx){
		friendService.accept(getPrincipal().getId(), friendsIdx);
		return new ResponseEntity<>("친구 요청 수락", HttpStatus.OK);
	}

	@ApiOperation(value="Denying the request for being a friend")
	@DeleteMapping("/friends/request/{friendsIdx}")
	public ResponseEntity<String> denyRequest(@PathVariable int friendsIdx){
		friendService.deny(getPrincipal().getId(), friendsIdx);
		return new ResponseEntity<>("친구 요청 거절", HttpStatus.OK);
	}

	@ApiOperation(value="getting all the requests for being a friend")
	@GetMapping("/friends/request")
	public List<FriendsReqListDto> getAllRequests(){

		return friendService.getAllRequests(getPrincipal().getId());
	}
	
	public User getPrincipal() {
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;
		return principal.getUser();
	}
}
