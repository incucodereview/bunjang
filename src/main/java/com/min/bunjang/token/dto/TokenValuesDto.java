package com.min.bunjang.token.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenValuesDto {
    private String accessToken;
    private String refreshToken;

    public TokenValuesDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenValuesDto of(String accessToken, String refreshToken) {
        return new TokenValuesDto(accessToken, refreshToken);
    }
}
