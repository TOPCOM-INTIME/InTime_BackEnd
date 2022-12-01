package com.topcom.intime.firebase;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.topcom.intime.Dto.ResponseDto;
import com.topcom.intime.auth.PrincipalDetails;
import com.topcom.intime.model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FcmController {

	private final FirebaseCloudMessageService firebaseCloudMessageService;

    @PostMapping("/api/fcm/schedule")
    public ResponseDto<Integer> inviteSchedule(@RequestBody InviteDto inviteDto) throws IOException {

        int is_success = firebaseCloudMessageService.sendMessageForSchedule(inviteDto, getPrincipal().getUsername());
        
        if (is_success == 1 ) {
    		return new ResponseDto<Integer>(HttpStatus.OK.value(), is_success);
        }
		return new ResponseDto<Integer>(HttpStatus.BAD_REQUEST.value(), is_success);
    }
    
    @PostMapping("/api/fcm/friend")
    public ResponseDto<Integer> inviteFriend(@RequestBody InviteDto inviteDto) throws IOException {

        int is_success = firebaseCloudMessageService.sendMessageForFriend(inviteDto, getPrincipal().getUsername());
        
        if (is_success == 1 ) {
    		return new ResponseDto<Integer>(HttpStatus.OK.value(), is_success);
        }
		return new ResponseDto<Integer>(HttpStatus.BAD_REQUEST.value(), is_success);
    }

	public User getPrincipal() {
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;
		return principal.getUser();
	}
	
}
