package com.tys.survey.service;

import org.springframework.beans.factory.annotation.Value;


import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String sendUserEmail;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    public EmailService(JavaMailSender javaMailSender, MessageSource messageSource) {
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
    }

    public Map<String, Object> sendMail(Map<String, Object> map, String memberEmail){
        String randomKey = randomKey();
        Date currentTime = new Date();
        long validTime = 5 * 60 * 1000;
        Date expirationTime = new Date(currentTime.getTime() + validTime);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(memberEmail);
        simpleMailMessage.setSubject("설사 이메일 인증 코드");
        simpleMailMessage.setFrom(sendUserEmail);
        simpleMailMessage.setText("인증번호 (  " + randomKey + "  )를 인증해주세요.");

        try {
            javaMailSender.send(simpleMailMessage);
        }catch (MailException e){
            System.out.println("MailException :"+e);
            return createEmailFailMsg(map,messageSource.getMessage("isRetryAfter",null, Locale.getDefault()));
        }
        return createEmailTrueMsg(map,randomKey,expirationTime);
    }

    public String randomKey() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 10);
    }

    private Map<String, Object> createEmailFailMsg(Map<String, Object> map, String msg){
        map.put("result", false);
        map.put("message", msg);
        return map;
    }

    private Map<String, Object> createEmailTrueMsg(Map<String, Object> map, String randomKey, Date expirationTime){
        map.put("result", true);
        map.put("status",false);
        map.put("randomKey", randomKey);
        map.put("expirationTime", expirationTime);
        return map;
    }
}
