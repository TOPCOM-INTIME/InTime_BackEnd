package com.topcom.intime.firebase;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.topcom.intime.Dto.ResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FcmController {

	private final FirebaseCloudMessageService firebaseCloudMessageService;

    @PostMapping("/api/fcm")
    public ResponseDto<Integer> pushMessage(@RequestBody RequestForFcmDto requestDto) throws IOException {

        int is_success = firebaseCloudMessageService.sendMessageTo(requestDto);
        
        if (is_success == 1 ) {
    		return new ResponseDto<Integer>(HttpStatus.OK.value(), is_success);
        }
		return new ResponseDto<Integer>(HttpStatus.BAD_REQUEST.value(), is_success);
    }

	
}
