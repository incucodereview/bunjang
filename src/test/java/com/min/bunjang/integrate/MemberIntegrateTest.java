package com.min.bunjang.integrate;

import com.min.bunjang.config.IntegrateBaseTest;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.member.controller.MemberControllerPath;
import com.min.bunjang.member.dto.MemberBirthDayUpdateRequest;
import com.min.bunjang.member.dto.MemberGenderUpdateRequest;
import com.min.bunjang.member.dto.MemberPhoneUpdateRequest;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.token.jwt.TokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberIntegrateTest extends IntegrateBaseTest {

    @DisplayName("회원성별 변경 통합테스트")
    @Test
    public void 회원_성별_변경() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto tokenValuesDto = MemberHelper.로그인(email, password, loginService);

        MemberGenderUpdateRequest memberGenderUpdateRequest = new MemberGenderUpdateRequest(MemberGender.WOMEN);

        //when
        String path = MemberControllerPath.MEMBER_CHANGE_GENDER.replace("{memberNum}", String.valueOf(member.getMemberNum()));
        mockMvc.perform(patch(path)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, tokenValuesDto.getAccessToken())
                        .content(objectMapper.writeValueAsString(memberGenderUpdateRequest)))
                .andExpect(status().isOk());

        //then
        Member findMember = memberRepository.findById(member.getMemberNum()).get();
        Assertions.assertThat(findMember.getMemberGender()).isEqualTo(memberGenderUpdateRequest.getMemberGender());
    }

    @DisplayName("회원 생년월일 변경 통합테스트")
    @Test
    public void 회원_생년월일_변경() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto tokenValuesDto = MemberHelper.로그인(email, password, loginService);

        LocalDate birthDate = LocalDate.of(2000, 10, 10);
        MemberBirthDayUpdateRequest memberBirthDayUpdateRequest = new MemberBirthDayUpdateRequest(birthDate);

        //when
        String path = MemberControllerPath.MEMBER_CHANGE_BIRTHDAY.replace("{memberNum}", String.valueOf(member.getMemberNum()));
        mockMvc.perform(patch(path)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, tokenValuesDto.getAccessToken())
                        .content(objectMapper.writeValueAsString(memberBirthDayUpdateRequest)))
                .andExpect(status().isOk());

        //then
        Member findMember = memberRepository.findById(member.getMemberNum()).get();
        Assertions.assertThat(findMember.getBirthDate()).isEqualTo(memberBirthDayUpdateRequest.getBirthDate());
    }

    @DisplayName("회원 폰넘버 변경 통합테스트")
    @Test
    public void 회원_폰넘버_변경() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto tokenValuesDto = MemberHelper.로그인(email, password, loginService);

        String phoneNum = "010-3333-8888";
        MemberPhoneUpdateRequest memberPhoneUpdateRequest = new MemberPhoneUpdateRequest(phoneNum);

        //when
        String path = MemberControllerPath.MEMBER_CHANGE_PHONE.replace("{memberNum}", String.valueOf(member.getMemberNum()));
        mockMvc.perform(patch(path)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, tokenValuesDto.getAccessToken())
                        .content(objectMapper.writeValueAsString(memberPhoneUpdateRequest)))
                .andExpect(status().isOk());

        //then
        Member findMember = memberRepository.findById(member.getMemberNum()).get();
        Assertions.assertThat(findMember.getPhone()).isEqualTo(memberPhoneUpdateRequest.getPhone());
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
