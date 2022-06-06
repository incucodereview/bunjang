package com.min.bunjang.common.email;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class EmailGenerator {
    private static final String CONFIRM_EMAIL_SUBJECT = "회원가입 이메일 인증";
    private static final String CONFIRM_EMAIL_CONTENT = "회원가입 인증 이메일 입니다. 아래 링크를 클릭해주세요";
    private static final String CONFIRM_EMAIL_LINK_LOCAL = "http://localhost:8080/confirm-email?token=";
    private static final String CONFIRM_EMAIL_LINK_LIVE = "http://http://ec2-3-34-52-134.ap-northeast-2.compute.amazonaws.com:8080//confirm-email?token=";
    private static final String SENDER_EMAIL_ADDRESS = "jminyeong96@gmail.com";

    private final Environment environment;

    public static SimpleMailMessage generateSimpleMailMessage(String receiverEmail, String tokenValue) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiverEmail);
        mailMessage.setFrom(SENDER_EMAIL_ADDRESS);
        mailMessage.setSubject(CONFIRM_EMAIL_SUBJECT);
        mailMessage.setText(CONFIRM_EMAIL_CONTENT + "\n" + CONFIRM_EMAIL_LINK_LOCAL + tokenValue);
        return mailMessage;
    }
}
