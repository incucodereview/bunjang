package com.min.bunjang.login.service;

import com.min.bunjang.login.dto.LoginRequest;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.token.model.RefreshToken;
import com.min.bunjang.token.repository.RefreshTokenRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

@ActiveProfiles("h2")
@SpringBootTest
class LoginServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private LoginService loginService;

    @Test
    @DisplayName("로그인 로직이 정상동작한다.")
    void login_ok() {
        //given
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String email = "email";
        String password = "password";
        Member member = Member.createMember(MemberDirectCreateDto.of(
                email,
                bCryptPasswordEncoder.encode(password),
                "min",
                "phone",
                LocalDate.of(1996, 10, 14),
                MemberRole.ROLE_ADMIN
        ));
        memberRepository.save(member);

        LoginRequest loginRequest = new LoginRequest(email, password);
        //when
        TokenValuesDto tokenValuesDto = loginService.login(loginRequest);
        //then
        Assertions.assertThat(tokenValuesDto.getAccessToken()).isNotNull();
        Assertions.assertThat(tokenValuesDto.getRefreshToken()).isNotNull();

        RefreshToken refreshToken = refreshTokenRepository.findById(email).orElseThrow(NotExistMemberException::new);
        Assertions.assertThat(tokenValuesDto.getRefreshToken()).isEqualTo(refreshToken.getRefreshToken());
    }

    @DisplayName("[예외] 없는 이메일로 로그인 시도시 NotExistMemberException 예외가 발생한다.")
    @Test
    void login_NotExistMember() {
        //given
        String email = "email";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(email, password);

        //when & then
        Assertions.assertThatThrownBy(() -> loginService.login(loginRequest)).isInstanceOf(NotExistMemberException.class);

    }
}