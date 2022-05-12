package com.min.bunjang.token.service;

import com.min.bunjang.common.database.DatabaseFormat;
import com.min.bunjang.token.jwt.TokenProvider;
import com.min.bunjang.token.jwt.properties.JwtTokenProperty;
import com.min.bunjang.token.dto.TokenValidResponse;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.token.exception.ExpiredRefreshTokenException;
import com.min.bunjang.token.exception.NotExistRefreshTokenException;
import com.min.bunjang.token.model.RefreshToken;
import com.min.bunjang.token.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Base64;
import java.util.Date;


@SpringBootTest
@ActiveProfiles("h2")
class TokenServiceTest {
    @Autowired
    private DatabaseFormat databaseFormat;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private JwtTokenProperty jwtTokenProperty;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @DisplayName("만료되지 않은 엑세스 토큰을 검증 요청하면 알맞는 응답을 한다.")
    @Test
    void handleToken_notExpiredAccess() {
        //given
        String email = "email@email.com";
        String accessToken = tokenProvider.createAccessToken(email);
        String refreshToken = tokenProvider.createRefreshToken(email);
        TokenValuesDto tokenValuesDto = new TokenValuesDto(accessToken, refreshToken);

        refreshTokenRepository.save(RefreshToken.of(email, refreshToken));

        //when
        TokenValidResponse tokenValidResponse = tokenService.handleTokenOnTokenStatus(tokenValuesDto);

        //then
        Assertions.assertThat(tokenValidResponse.isReissuedAccessToken()).isFalse();
        Assertions.assertThat(tokenValidResponse.getAccessToken()).isEqualTo(accessToken);
    }

    @DisplayName("만료된 엑세스 토큰과 만료되지 않은 리프레시 토큰을 검증 요청하면 새로운 엑세스 토큰을 응답한다.")
    @Test
    void handleToken_expiredAccessAndNotExpiredRefresh() {
        //given
        String email = "email@email.com";
        String tempAccessServerKey = Base64.getEncoder().encodeToString(jwtTokenProperty.getAccessKey().getBytes());
        String tempRefreshServerKey = Base64.getEncoder().encodeToString(jwtTokenProperty.getRefreshKey().getBytes());

        Date now = new Date();
        Date expired = new Date(now.getTime() - 1000);
        Date expiredForRefresh = new Date(now.getTime() + 1209600000);

        Claims claims = Jwts.claims();
        claims.put("email", email);

        String tempAccessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS256, tempAccessServerKey)
                .compact();

        String tempRefreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredForRefresh)
                .signWith(SignatureAlgorithm.HS256, tempRefreshServerKey)
                .compact();
        refreshTokenRepository.save(RefreshToken.of(email, tempRefreshToken));

        //when
        TokenValidResponse tokenValidResponse = tokenService.handleTokenOnTokenStatus(TokenValuesDto.of(tempAccessToken, tempRefreshToken));

        //then
        Assertions.assertThat(tokenValidResponse.getAccessToken()).isNotEqualTo(tempAccessToken);
        Assertions.assertThat(tokenValidResponse.isReissuedAccessToken()).isTrue();
    }

    @DisplayName("[예외] 만료된 엑세스 토큰과 만료된 리프레시 토큰을 검증 요청하면 만료된 리프레시 토큰이 삭제되고 알맞는 예외가 발생한다.")
    @Test
    void handleToken_expiredRefresh() {
        //given
        String email = "email@email.com";
        String tempAccessServerKey = Base64.getEncoder().encodeToString(jwtTokenProperty.getAccessKey().getBytes());
        String tempRefreshServerKey = Base64.getEncoder().encodeToString(jwtTokenProperty.getRefreshKey().getBytes());

        Date now = new Date();
        Date expired = new Date(now.getTime() - 1200);

        Claims claims = Jwts.claims();
        claims.put("email", email);

        String tempAccessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS256, tempAccessServerKey)
                .compact();

        String tempRefreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS256, tempRefreshServerKey)
                .compact();

        refreshTokenRepository.save(RefreshToken.of(email, tempRefreshToken));

        //when & then
        Assertions.assertThatThrownBy(() -> tokenService.handleTokenOnTokenStatus(TokenValuesDto.of(tempAccessToken, tempRefreshToken))).isInstanceOf(ExpiredRefreshTokenException.class);
    }

    @DisplayName("[예외] 리프레시 토큰이 디비에 없는 경우 로그아웃되고 알맞는 예외가 발생한다.")
    @Test
    void handleToken_notExistRefreshToken() {
        //given
        String email = "email@email.com";
        String tempAccessServerKey = Base64.getEncoder().encodeToString(jwtTokenProperty.getAccessKey().getBytes());
        String tempRefreshServerKey = Base64.getEncoder().encodeToString(jwtTokenProperty.getRefreshKey().getBytes());

        Date now = new Date();
        Date expired = new Date(now.getTime() - 12000);

        Claims claims = Jwts.claims();
        claims.put("email", email);

        String tempAccessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS256, tempAccessServerKey)
                .compact();

        String tempRefreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 120000))
                .signWith(SignatureAlgorithm.HS256, tempRefreshServerKey)
                .compact();

        //when & then
        Assertions.assertThatThrownBy(() -> tokenService.handleTokenOnTokenStatus(TokenValuesDto.of(tempAccessToken, tempRefreshToken))).isInstanceOf(NotExistRefreshTokenException.class);
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}