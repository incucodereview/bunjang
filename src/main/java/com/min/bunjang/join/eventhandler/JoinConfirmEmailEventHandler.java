package com.min.bunjang.join.eventhandler;

import com.min.bunjang.join.confirmtoken.model.ConfirmationToken;
import com.min.bunjang.join.confirmtoken.repository.ConfirmationTokenRepository;
import com.min.bunjang.join.email.service.EmailSenderService;
import com.min.bunjang.join.event.JoinEmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class JoinConfirmEmailEventHandler {
    private static final String CONFIRM_EMAIL_SUBJECT = "회원가입 이메일 인증";
    private static final String CONFIRM_EMAIL_CONTENT = "회원가입 인증 이메일 입니다. 아래 링크를 클릭해주세요";
    private static final String CONFIRM_EMAIL_LINK = "http://localhost:8080/confirm-email?token=";

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSenderService emailSenderService;

    @Async("threadPoolTaskExecutor")
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void sendEmailEvent(JoinEmailEvent joinEmailEvent) {
        log.info("이메일 전송 이벤트 실행");
        ConfirmationToken emailConfirmationToken = ConfirmationToken.createEmailConfirmationToken(joinEmailEvent.getMemberId());
        ConfirmationToken savedConfirmationToken = confirmationTokenRepository.save(emailConfirmationToken);
        log.info("이메일 확인 토큰 저장 완료");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(joinEmailEvent.getEmail());
        mailMessage.setSubject(CONFIRM_EMAIL_SUBJECT);
        mailMessage.setText(CONFIRM_EMAIL_CONTENT);
        mailMessage.setText(CONFIRM_EMAIL_LINK + savedConfirmationToken.getId());
        emailSenderService.sendMail(mailMessage);
        log.info("이메일 전송 완료");
    }
}
