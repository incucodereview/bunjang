package com.min.bunjang.join.confirmtoken.service;

import com.min.bunjang.join.confirmtoken.exception.WrongConfirmEmailToken;
import com.min.bunjang.join.confirmtoken.model.ConfirmationToken;
import com.min.bunjang.join.confirmtoken.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void verifyConfirmEmailToken(String token) {
        ConfirmationToken confirmationToken =
                confirmationTokenRepository.findByIdAndExpirationDateAfterAndExpired(token, LocalDateTime.now(), false).orElseThrow(WrongConfirmEmailToken::new);
        confirmationToken.useToken();

    }
}
