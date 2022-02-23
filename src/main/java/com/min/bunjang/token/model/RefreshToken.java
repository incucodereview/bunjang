package com.min.bunjang.token.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    private String email;

    private String refreshToken;

    private LocalDateTime createDate;

    private RefreshToken(String email, String refreshToken, LocalDateTime createDate) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.createDate = createDate;
    }

    public static RefreshToken of(String email, String refreshToken) {
        return new RefreshToken(email, refreshToken, LocalDateTime.now());
    }
}
