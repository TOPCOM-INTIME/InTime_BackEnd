package com.topcom.intime.service;

import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.UserRepository;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender emailSender;
    public EmailService(JavaMailSender emailSender){
        this.emailSender=emailSender;
    }

    private MimeMessage createMessage(String to, String code) throws Exception{
        MimeMessage message=emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("인타임 어플 비밀번호");

        String msg="회원의 비밀번호는: "+code+"입니다.";
        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("intimeajou@gmail.com", "인타임"));
        return message;

    }

    public String sendMessage(String to, String code) throws Exception{
        MimeMessage message=createMessage(to, code);
        try{
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();;
            throw new IllegalArgumentException();
        }
        return code;
    }

}
