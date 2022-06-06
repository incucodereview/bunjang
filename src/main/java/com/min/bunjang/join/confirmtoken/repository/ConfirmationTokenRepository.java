package com.min.bunjang.join.confirmtoken.repository;

import com.min.bunjang.join.confirmtoken.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
    Optional<ConfirmationToken> findByIdAndExpirationDateAfterAndExpired(String confirmationTokenId, LocalDateTime now, boolean expired);
    Optional<ConfirmationToken> findByEmail(String email);
}
