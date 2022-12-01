package com.topcom.intime.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@SpringBootTest
@WebAppConfiguration
class EmailServiceTest {
    @Autowired
    private JavaMailSender javaMailSender;
    @Test
    void sendMailTest() throws Exception{
        String code="ae758";

        MimeMessage message=javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, "jtiger0303@ajou.ac.kr");
        message.setText("임시 비밀번호");
        String msg="회원의 임시 비밀번호는: "+code+"입니다. 다른 비밀번호로 변경하기를 권장합니다.";
        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("intimeajou@gmail.com", "인타임"));

        Assertions.assertThat(code).isNotEmpty();
    }
}
