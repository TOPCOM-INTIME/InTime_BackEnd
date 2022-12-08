package com.topcom.intime.service;

import com.topcom.intime.exception.APIException;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Timestamp;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void join(){
        User user=new User();
        user.setCreateDate(Timestamp.valueOf("2022-11-16 02:31:52.981000"));
        user.setEmail("test2@test.com");
        user.setOauth(null);
        user.setPassword("1234");
        user.setRoles("ROLE_USER");
        user.setUsername("test2");
        user.setDeviceToken(null);
        userRepository.save(user);
        assertThat(user).isNotNull();
    }

    @Test
    public void updatePwd() {
        User user=userRepository.findByEmail("test2@test.com").orElseThrow(()->new APIException(HttpStatus.NOT_FOUND, "잘못된 이메일"));
        user.setPassword("12345");
        assertThat(user.getPassword()).isEqualTo("12345");
    }

    @Test
    public void updateUsername() {
        User user=userRepository.findByUsername("test2").orElseThrow(()->new APIException(HttpStatus.NOT_FOUND, "존재하지 않은 닉네임입니다."));
        user.setUsername("test3");
        assertThat(user.getUsername()).isEqualTo("test3");
    }

}