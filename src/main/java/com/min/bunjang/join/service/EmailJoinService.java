package com.min.bunjang.join.service;

import com.min.bunjang.join.confirmtoken.exception.WrongConfirmEmailToken;
import com.min.bunjang.join.confirmtoken.model.ConfirmationToken;
import com.min.bunjang.join.confirmtoken.repository.ConfirmationTokenRepository;
import com.min.bunjang.join.event.JoinEmailEvent;
import com.min.bunjang.join.dto.EmailJoinRequest;
import com.min.bunjang.member.model.JoinTempMember;
import com.min.bunjang.member.repository.JoinTempMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailJoinService {
    private final JoinTempMemberRepository joinTempMemberRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public void joinTempMember(EmailJoinRequest emailJoinRequest) {
        JoinTempMember joinTempMember = JoinTempMember.createJoinTempMember(
                emailJoinRequest.getEmail(),
                bCryptPasswordEncoder.encode(emailJoinRequest.getPassword()),
                emailJoinRequest.getName(),
                emailJoinRequest.getPhone(),
                emailJoinRequest.getBirthDate()
        );
        JoinTempMember savedJoinTempMember = joinTempMemberRepository.save(joinTempMember);

        eventPublisher.publishEvent(new JoinEmailEvent(this, savedJoinTempMember.getEmail()));
    }

    @Transactional
    public void verifyConfirmEmailToken(String token) {
        ConfirmationToken confirmationToken =
                confirmationTokenRepository.findByIdAndExpirationDateAfterAndExpired(token, LocalDateTime.now(), false).orElseThrow(WrongConfirmEmailToken::new);
        confirmationToken.useToken();
    }
}
