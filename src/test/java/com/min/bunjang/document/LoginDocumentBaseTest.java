package com.min.bunjang.document;

import com.min.bunjang.config.DocumentBaseTest;
import com.min.bunjang.login.controller.LoginControllerPath;
import com.min.bunjang.login.dto.LoginRequest;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginDocumentBaseTest extends DocumentBaseTest {
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("로그인 기능 통합테스트")
    @Test
    void login_integrate() throws Exception {
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
                MemberRole.ROLE_ADMIN,
                MemberGender.MEN
        ));
        memberRepository.save(member);

        LoginRequest loginRequest = new LoginRequest(email, password);

        //when & then
        mockMvc.perform(post(LoginControllerPath.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andDo(document("login",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("email").description("회원 아이디 필드"),
                                fieldWithPath("password").description("회원 비밀번호 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청에 대한 응답 상태코드"),
                                fieldWithPath("message").description("Exception 발생시 메세지 필드"),
                                fieldWithPath("result.accessToken").description("로그인 완료한 회원의 엑세스 토큰"),
                                fieldWithPath("result.refreshToken").description("로그인 완료한 회원의 리프레시 토큰")
                        )
                ));
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
