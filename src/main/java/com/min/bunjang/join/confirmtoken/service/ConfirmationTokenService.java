package com.min.bunjang.join.confirmtoken.service;

import com.min.bunjang.join.confirmtoken.exception.WrongConfirmEmailToken;
import com.min.bunjang.join.confirmtoken.model.ConfirmationToken;
import com.min.bunjang.join.confirmtoken.repository.ConfirmationTokenRepository;
import com.min.bunjang.join.dto.EmailConfirmRequest;
import com.min.bunjang.join.email.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSenderService emailSenderService;
    private static final String CONFIRM_EMAIL_SUBJECT = "회원가입 이메일 인증";
    private static final String CONFIRM_EMAIL_CONTENT = "회원가입 인증 이메일 입니다. 아래 링크를 클릭해주세요";
    private static final String CONFIRM_EMAIL_LINK = "http://localhost:8080/confirm-email?token=";

    @Transactional
    public void sendConfirmEmailForJoin(EmailConfirmRequest emailConfirmRequest) {
        ConfirmationToken emailConfirmationToken = ConfirmationToken.createEmailConfirmationToken(emailConfirmRequest.getEmail());
        ConfirmationToken savedConfirmationToken = confirmationTokenRepository.save(emailConfirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailConfirmRequest.getEmail());
        mailMessage.setSubject(CONFIRM_EMAIL_SUBJECT);
        mailMessage.setText(CONFIRM_EMAIL_CONTENT);
        mailMessage.setText(CONFIRM_EMAIL_LINK+savedConfirmationToken.getId());
        emailSenderService.sendMail(mailMessage);

    }

    public void verifyConfirmEmailToken(String token) {
        ConfirmationToken confirmationToken =
                confirmationTokenRepository.findByIdAndExpirationDateAfterAndExpired(token, LocalDateTime.now(), false).orElseThrow(WrongConfirmEmailToken::new);
        confirmationToken.useToken();

    }
}
