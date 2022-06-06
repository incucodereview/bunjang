package com.min.bunjang.token.jwt;

import com.min.bunjang.token.jwt.properties.JwtTokenProperty;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("h2")
@SpringBootTest
class JwtTokenPropertyTest {

    @Autowired
    private JwtTokenProperty jwtTokenProperty;

    @Test
    @Disabled
    @DisplayName("security.yml 파일의 내용을 읽어 올수 있다")
    void name() {
        String accessKey = jwtTokenProperty.getAccessKey();
        String refreshKey = jwtTokenProperty.getRefreshKey();
        Long accessExpired = jwtTokenProperty.getAccessExpired();
        Long refreshExpired = jwtTokenProperty.getRefreshExpired();
    }
}