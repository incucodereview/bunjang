package com.min.bunjang.integrate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.config.IntegrateBaseTest;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.login.controller.LoginControllerPath;
import com.min.bunjang.login.dto.LoginRequest;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginIntegrateTest extends IntegrateBaseTest {

    @DisplayName("회원가입 통합테스트")
    @Test
    public void 회원가입() throws Exception {
        //given
        String email = "email";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);

        LoginRequest loginRequest = new LoginRequest(email, password);

        //when
        mockMvc.perform(post(LoginControllerPath.LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
