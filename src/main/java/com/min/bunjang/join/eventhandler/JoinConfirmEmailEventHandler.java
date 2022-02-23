package com.min.bunjang.join.eventhandler;

import com.min.bunjang.join.confirmtoken.model.ConfirmationToken;
import com.min.bunjang.join.confirmtoken.repository.ConfirmationTokenRepository;
import com.min.bunjang.join.event.JoinEmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jta.atomikos.AtomikosDependsOnBeanFactoryPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JoinConfirmEmailEventHandler {
    private static final String CONFIRM_EMAIL_SUBJECT = "회원가입 이메일 인증";
    private static final String CONFIRM_EMAIL_CONTENT = "회원가입 인증 이메일 입니다. 아래 링크를 클릭해주세요";
    private static final String CONFIRM_EMAIL_LINK = "http://localhost:8080/confirm-email?token=";

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final JavaMailSender javaMailSender;
    private final Environment environment;

    @Async
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void sendEmailEvent(JoinEmailEvent joinEmailEvent) {
        log.info("이메일 전송 이벤트 실행");

        ConfirmationToken emailConfirmationToken = ConfirmationToken.createEmailConfirmationToken(joinEmailEvent.getEmail());
        ConfirmationToken savedConfirmationToken = confirmationTokenRepository.save(emailConfirmationToken);
        log.info("이메일 확인 토큰 저장 완료");

        //Todo : 밑의 이메일 메세지 만드는 작업 따로 클래스로 빼서 사용해볼것.
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(joinEmailEvent.getEmail());
        mailMessage.setFrom("jminyeong96@gmail.com");
        mailMessage.setSubject(CONFIRM_EMAIL_SUBJECT);
        mailMessage.setText(CONFIRM_EMAIL_CONTENT + "\n" + CONFIRM_EMAIL_LINK + savedConfirmationToken.getId());
        if (!Arrays.asList(environment.getActiveProfiles()).contains("h2")) {
            javaMailSender.send(mailMessage);
        }
        log.info("이메일 전송 완료");
    }
}
