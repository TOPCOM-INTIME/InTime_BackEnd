package com.topcom.intime.service;

import com.topcom.intime.exception.APIException;
import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void join(){
        User user=new User();
        user.setCreateDate(Timestamp.valueOf("2022-11-16 02:31:52.981000"));
        user.setEmail("test@test.com");
        user.setOauth(null);
        user.setPassword("1234");
        user.setRoles("ROLE_USER");
        user.setUsername("test");
        user.setDeviceToken(null);
        userRepository.save(user);
        assertThat(user).isNotNull();
    }
    @Test
    void updatePwd() {
        User user=userRepository.findByEmail("test@test.com").orElseThrow(()->new APIException(HttpStatus.NOT_FOUND, "잘못된 이메일"));
        user.setPassword("12345");
        assertThat(user.getPassword()).isEqualTo("12345");
    }

    @Test
    void updateUsername() {
        User user=userRepository.findByUsername("test").orElseThrow(()->new APIException(HttpStatus.NOT_FOUND, "존재하지 않은 닉네임입니다."));
        user.setUsername("test2");
        assertThat(user.getUsername()).isEqualTo("test2");
    }
}