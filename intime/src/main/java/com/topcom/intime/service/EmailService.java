package com.topcom.intime.service;

import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.UserRepository;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

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

        String msg="회원의 임시 비밀번호는: "+code+"입니다. 다른 비밀번호로 변경하기를 권장합니다.";
        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("intimeajou@gmail.com", "인타임"));
        return message;

    }

    private MimeMessage createAuthMessage(String to, String code) throws Exception{
        MimeMessage message=emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("인타임 어플 인증 비밀번호");

        String msg="회원의 어플 인증 번호는: "+code+"입니다. 빠른 시간내에 입력해주시기 바랍니다.";
        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("intimeajou@gmail.com", "인타임"));
        return message;

    }
    public String sendMessage(String to, String code) throws Exception{
        MimeMessage message=createMessage(to, code);
        try{
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return code;
    }
    public String sendAuthMessage(String to, String code) throws Exception{
        MimeMessage message=createAuthMessage(to, code);
        try{
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return code;
    }

    public String createKey(){
        StringBuffer key= new StringBuffer();
        Random rnd= new Random();

        for(int i=0; i<8; i++){ //임시 비밀번호 8자리
            int index=rnd.nextInt(3);
            switch(index){
                case 0:
                    key.append((char)((int)(rnd.nextInt(26))+97));
                    // a~z
                    break;
                case 1:
                    key.append((char)((int)(rnd.nextInt(26))+65));
                    //A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    //0~9
                    break;
            }
        }
        return key.toString();
    }

}
