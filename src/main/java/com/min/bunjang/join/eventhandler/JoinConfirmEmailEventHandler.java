package com.min.bunjang.join.eventhandler;

import com.min.bunjang.join.confirmtoken.model.ConfirmationToken;
import com.min.bunjang.join.confirmtoken.repository.ConfirmationTokenRepository;
import com.min.bunjang.common.email.EmailGenerator;
import com.min.bunjang.join.event.JoinEmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        SimpleMailMessage mailMessage = EmailGenerator.generateSimpleMailMessage(joinEmailEvent.getEmail(), savedConfirmationToken.getId());
        if (!Arrays.asList(environment.getActiveProfiles()).contains("h2")) {
            javaMailSender.send(mailMessage);
        }
        log.info("이메일 전송 완료");
    }
}
