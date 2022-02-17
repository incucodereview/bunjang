package com.min.bunjang.login.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenProvider {

    private String accessTokenValue;
    private String refreshTokenValue;
    private Long accessExpired;
    private Long refreshExpired;

    public TokenProvider(JwtTokenProperty jwtTokenProperty) {
        this.accessTokenValue = accessTokenValue;
        this.refreshTokenValue = refreshTokenValue;
        this.accessExpired = accessExpired;
        this.refreshExpired = refreshExpired;
    }
}
