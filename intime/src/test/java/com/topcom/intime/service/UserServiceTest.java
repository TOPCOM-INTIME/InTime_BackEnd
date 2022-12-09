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
import java.util.Random;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@WebAppConfiguration
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void join(){
        User user=new User();
        user.setCreateDate(Timestamp.valueOf("2022-11-16 02:31:52.981000"));
        user.setEmail("test3@test.com");
        user.setOauth(null);
        user.setPassword("1234");
        user.setRoles("ROLE_USER");
        user.setUsername("test3");
        user.setDeviceToken(null);
        userRepository.save(user);
        assertThat(user).isNotNull();
        userRepository.delete(user);
    }
    @Test
    void updatePwd() {
        User user=userRepository.findByEmail("jtiger0303@ajou.ac.kr").orElseThrow(()->new APIException(HttpStatus.NOT_FOUND, "잘못된 이메일"));
        StringBuffer newPwd= new StringBuffer();
        Random rnd= new Random();

        for(int i=0; i<8; i++){ //임시 비밀번호 8자리
            int index=rnd.nextInt(3);
            switch(index){
                case 0:
                    newPwd.append((char)((int)(rnd.nextInt(26))+97));
                    // a~z
                    break;
                case 1:
                    newPwd.append((char)((int)(rnd.nextInt(26))+65));
                    //A~Z
                    break;
                case 2:
                    newPwd.append((rnd.nextInt(10)));
                    //0~9
                    break;
            }
        }
        user.setPassword(newPwd.toString());
        assertThat(user.getPassword()).isNotNull();
    }

}
