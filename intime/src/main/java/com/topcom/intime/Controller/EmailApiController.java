package com.topcom.intime.Controller;

import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.UserRepository;
import com.topcom.intime.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailApiController {

    @Autowired
    private UserRepository userRepository;

    private final EmailService emailService;

    public EmailApiController(UserRepository userRepository, EmailService emailService){
        this.userRepository=userRepository;
        this.emailService=emailService;
    }

    @GetMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestParam String email){
        try{
            User user=userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "email", Long.parseLong(email)));
            String code=user.getPassword();
            emailService.sendMessage(email, code);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("이메일 전송 실패", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("이메일 전송 성공", HttpStatus.OK);
    }
}
