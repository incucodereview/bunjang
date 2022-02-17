package com.min.bunjang.login.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "security.jwt.secret-key")
public class JwtTokenProperty {
    private final String accessKey;
    private final String refreshKey;
    private final Long accessExpired;
    private final Long refreshExpired;
}
