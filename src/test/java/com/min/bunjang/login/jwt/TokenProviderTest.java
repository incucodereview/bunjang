package com.min.bunjang.login.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Base64;
import java.util.Date;

@ActiveProfiles("h2")
@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private JwtTokenProperty jwtTokenProperty;

    @Test
    @DisplayName("요청온 토큰에서 email 값을 추출 할 수 있다")
    void name() {
        //given
        String email = "min@emial.com";
        Claims claims = Jwts.claims();
        claims.put("email", email);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(jwtTokenProperty.getAccessKey().getBytes()))
                .compact();
        //when
        String emailFromAccessToken = tokenProvider.getEmailFromAccessToken(token);
        //then
        Assertions.assertThat(emailFromAccessToken).isEqualTo(email);
    }
}