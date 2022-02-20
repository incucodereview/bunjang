package com.min.bunjang.acceptance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.common.AcceptanceTestConfig;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.login.controller.LoginControllerPath;
import com.min.bunjang.login.dto.LoginRequest;
import com.min.bunjang.login.dto.LoginResponse;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.awt.*;
import java.time.LocalDate;

@ActiveProfiles("h2")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginAcceptanceTest extends AcceptanceTestConfig {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void name() {
        //given
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String email = "email";
        String password = "password";
        Member member = Member.createMember(new MemberDirectCreateDto(null,
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
        RestResponse<LoginResponse> loginResponse = postApi(LoginControllerPath.LOGIN, loginRequest, new TypeReference<RestResponse<LoginResponse>>() {}, "");
        //then
        Assertions.assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(loginResponse.getResult().getAccessToken()).isNotNull();
        Assertions.assertThat(loginResponse.getResult().getRefreshToken()).isNotNull();
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}
