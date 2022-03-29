package com.min.bunjang.join.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.min.bunjang.join.dto.JoinRequest;
import com.min.bunjang.join.dto.TempJoinRequest;
import com.min.bunjang.join.service.EmailJoinService;
import com.min.bunjang.login.jwt.TokenProvider;
import com.min.bunjang.member.exception.NotExistTempMemberException;
import com.min.bunjang.member.model.MemberGender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("h2")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = EmailJoinController.class)
class EmailJoinControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmailJoinService emailJoinService;

    @MockBean
    private TokenProvider tokenProvider;

    @DisplayName("임시 회원가입 요청에 200 코드를 응답한다")
    @Test
    void join_tempMember() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        String name = "name";
        String phone = "phone";
        LocalDate birthDate = LocalDate.of(2000, 10, 10);

        TempJoinRequest tempJoinRequest = new TempJoinRequest(email, password, name, phone, birthDate);

        //when & then
        mockMvc.perform(post(EmailJoinControllerPath.JOIN_TEMP_MEMBER_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(tempJoinRequest)))
                .andExpect(status().isOk());
    }

    @DisplayName("[예외] 임시 회원가입 요청 내용 모두 null 이거나 공백문자일때 404를 응답한다")
    @ParameterizedTest
    @CsvSource(value = "' ',' ',' ',' '")
    void join_TempMember_BadRequest(String email, String password, String name, String phone) throws Exception {
        //given
        LocalDate birthDate = null;

        TempJoinRequest tempJoinRequest = new TempJoinRequest(email, password, name, phone, birthDate);

        //when & then
        mockMvc.perform(post(EmailJoinControllerPath.JOIN_TEMP_MEMBER_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(tempJoinRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("이메일인증 요청에 200 코드를 응답한다")
    @Test
    void join_Member() throws Exception {
        //given
        String token = "token";


        //when & then
        mockMvc.perform(get(EmailJoinControllerPath.CONFIRM_EMAIL_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("token", token))
                .andExpect(status().isOk());
    }

    @DisplayName("[예외] 회원가입 요청시 임시회원이 없는경우 400코드와 NotExistTempMemberException 을 응답한다")
    @Test
    void join_NotExistTempMemberException() throws Exception {
        //given
        JoinRequest joinRequest = new JoinRequest("email", MemberGender.WOMEN);

        doThrow(new NotExistTempMemberException()).when(emailJoinService).joinMember(joinRequest);
        //when & then
        mockMvc.perform(post(EmailJoinControllerPath.JOIN_MEMBER_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(joinRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").isString());
    }
}