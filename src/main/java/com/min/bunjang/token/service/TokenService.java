package com.min.bunjang.token.service;

import com.min.bunjang.token.jwt.TokenProvider;
import com.min.bunjang.token.dto.TokenValidResponse;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.token.exception.ExpiredRefreshTokenException;
import com.min.bunjang.token.exception.NotExistRefreshTokenException;
import com.min.bunjang.token.model.RefreshToken;
import com.min.bunjang.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    //TODO 방식이 조금 보안적으로 허술한듯.
    @Transactional
    public TokenValidResponse handleTokenOnTokenStatus(TokenValuesDto tokenValuesDto) {
        if (tokenProvider.isExpiredAccessToken(tokenValuesDto.getAccessToken(), new Date())) {
            String reissuedAccessToken = reissueAccessToken(tokenValuesDto);
            return new TokenValidResponse(reissuedAccessToken, true);
        }

        return new TokenValidResponse(tokenValuesDto.getAccessToken(), false);
    }

    private String reissueAccessToken(TokenValuesDto tokenValuesDto) {
        if (tokenProvider.isExpiredRefreshToken(tokenValuesDto.getRefreshToken(), new Date())) {
            throw new ExpiredRefreshTokenException();
        }

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(tokenValuesDto.getRefreshToken()).orElseThrow(NotExistRefreshTokenException::new);
        return tokenProvider.createAccessToken(refreshToken.getEmail());
    }
}
