package com.min.bunjang.login.service;

import com.min.bunjang.login.dto.LoginRequest;
import com.min.bunjang.login.dto.LoginResponse;
import com.min.bunjang.login.exception.NotMacheEmailAndPasswordException;
import com.min.bunjang.login.jwt.TokenProvider;
import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.token.model.RefreshToken;
import com.min.bunjang.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) throws RuntimeException{
        Member member = memberRepository.findByEmail(loginRequest.getEmail()).orElseThrow(NotExistMemberException::new);
        member.verifyMatchPassword(loginRequest.getPassword(), bCryptPasswordEncoder);

        String accessToken = tokenProvider.createAccessToken(member.getEmail());
        String refreshToken = tokenProvider.createRefreshToken(member.getEmail());
        refreshTokenRepository.save(RefreshToken.of(member.getEmail(), refreshToken));

        return LoginResponse.of(accessToken, refreshToken);
    }

}
