package com.min.bunjang.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.login.controller.LoginControllerPath;
import com.min.bunjang.login.dto.LoginRequest;
import com.min.bunjang.login.service.LoginService;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static com.min.bunjang.acceptance.common.AcceptanceTestConfig.postRequest;

public class MemberHelper {

    public static Member 회원가입(String email, String password, MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        MemberDirectCreateDto memberDirectCreateDto = MemberDirectCreateDto.of(
                email,
                bCryptPasswordEncoder.encode(password),
                "name",
                "phone",
                LocalDate.of(1996, 10, 14),
                MemberRole.ROLE_MEMBER,
                MemberGender.MEN
        );
        return memberRepository.save(Member.createMember(memberDirectCreateDto));
    }

    public static RestResponse<TokenValuesDto> 인수테스트_로그인(String email, String password) throws JsonProcessingException {
        return postRequest(LoginControllerPath.LOGIN, new LoginRequest(email, password), new TypeReference<RestResponse<TokenValuesDto>>() {
        }, "");
    }

    public static TokenValuesDto 로그인(String email, String password, LoginService loginService) {
        return loginService.login(new LoginRequest(email, password));
    }
}
