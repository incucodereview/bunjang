package com.min.bunjang.acceptance.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.acceptance.common.AcceptanceTestConfig;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.login.controller.LoginControllerPath;
import com.min.bunjang.login.dto.LoginRequest;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

public class LoginAcceptanceTest extends AcceptanceTestConfig {

    @Test
    void name() throws JsonProcessingException {
        //given
        String email = "email";
        String password = "password";
        Member member = Member.createMember(MemberDirectCreateDto.of(
                email,
                bCryptPasswordEncoder.encode(password),
                "min",
                "phone",
                LocalDate.of(1996, 10, 14),
                MemberRole.ROLE_ADMIN,
                MemberGender.MEN
        ));
        memberRepository.save(member);

        LoginRequest loginRequest = new LoginRequest(email, password);
        //when
        RestResponse<TokenValuesDto> loginResponse = postRequest(LoginControllerPath.LOGIN, loginRequest, new TypeReference<RestResponse<TokenValuesDto>>() {}, "");
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
