package com.min.bunjang.integrate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.min.bunjang.common.database.DatabaseCleanup;
import com.min.bunjang.integrate.config.IntegrateTestConfig;
import com.min.bunjang.join.confirmtoken.model.ConfirmationToken;
import com.min.bunjang.join.confirmtoken.repository.ConfirmationTokenRepository;
import com.min.bunjang.join.controller.EmailJoinControllerPath;
import com.min.bunjang.join.dto.JoinRequest;
import com.min.bunjang.join.dto.TempJoinRequest;
import com.min.bunjang.member.model.JoinTempMember;
import com.min.bunjang.member.repository.JoinTempMemberRepository;
import com.min.bunjang.testconfig.RestDocsConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JoinIntegrateTest extends IntegrateTestConfig {

    @Autowired
    private JoinTempMemberRepository joinTempMemberRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @DisplayName("임시회원 가입 요청 통합테스트")
    @Test
    void join_TempMember_integrate() throws Exception {
        //given
        String email = "email";
        String password = "password";
        String name = "name";
        String phone = "phone";
        LocalDate birthDate = LocalDate.of(1996, 10, 14);

        TempJoinRequest tempJoinRequest = new TempJoinRequest(email, password, name, phone, birthDate);

        //when & then
        mockMvc.perform(post(EmailJoinControllerPath.JOIN_TEMP_MEMBER_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(tempJoinRequest)))
                .andExpect(status().isOk())
                .andDo(document("email-temp-join",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("email").description("회원의 이메일 정보 필드."),
                                fieldWithPath("password").description("회원의 비밀번호 정보 필드."),
                                fieldWithPath("name").description("회원의 성명 정보 필드."),
                                fieldWithPath("phone").description("회원의 휴대전화 정보 필드."),
                                fieldWithPath("birthDate").description("회원의 생년월일 정보 필드.")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("응답의 데이터 필드.")
                        )
                ));
    }

    @DisplayName("이메일 인증 요청 통합테스트")
    @Test
    void join_confirmEmail_integrate() throws Exception {
        //given
        String email = "email";
        String password = "password";
        String name = "name";
        String phone = "phone";
        LocalDate birthDate = LocalDate.of(1996, 10, 14);

        TempJoinRequest tempJoinRequest = new TempJoinRequest(email, password, name, phone, birthDate);

        JoinTempMember joinTempMember = JoinTempMember.createJoinTempMember(tempJoinRequest, bCryptPasswordEncoder);
        JoinTempMember savedJoinTempMember = joinTempMemberRepository.save(joinTempMember);
        ConfirmationToken confirmationToken = confirmationTokenRepository.save(ConfirmationToken.createEmailConfirmationToken(savedJoinTempMember.getEmail()));

        //when & then
        mockMvc.perform(get(EmailJoinControllerPath.CONFIRM_EMAIL_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("token", confirmationToken.getId()))
                .andExpect(status().isOk())
                .andDo(document("confirm-email",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestParameters(
                                parameterWithName("token").description("임시회원 가입후 이메일로 전송된 인증용 토큰값")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("응답의 데이터 필드. 인증되면 정식회원 가입을 위한 id(이메일)을 반환.")
                        )
                ));
    }

    @DisplayName("정식회원 가입 요청 통합테스트")
    @Test
    void join_integrate() throws Exception {
        //given
        String email = "email";
        String password = "password";
        String name = "name";
        String phone = "phone";
        LocalDate birthDate = LocalDate.of(1996, 10, 14);

        TempJoinRequest tempJoinRequest = new TempJoinRequest(email, password, name, phone, birthDate);

        JoinTempMember joinTempMember = JoinTempMember.createJoinTempMember(tempJoinRequest, bCryptPasswordEncoder);
        JoinTempMember savedJoinTempMember = joinTempMemberRepository.save(joinTempMember);

        JoinRequest joinRequest = new JoinRequest(savedJoinTempMember.getEmail());

        //when & then
        mockMvc.perform(post(EmailJoinControllerPath.JOIN_MEMBER_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(joinRequest)))
                .andExpect(status().isOk())
                .andDo(document("email-join",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("email").description("이메일 인증후 응답된 회원 이메일 데이터.")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("응답의 데이터 필드.")
                        )
                ));
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}
