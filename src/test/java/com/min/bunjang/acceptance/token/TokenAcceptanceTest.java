package com.min.bunjang.acceptance.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.acceptance.common.AcceptanceTestConfig;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.token.controller.TokenControllerPath;
import com.min.bunjang.token.dto.TokenValidResponse;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TokenAcceptanceTest extends AcceptanceTestConfig {

    @DisplayName("토큰 인증 인수테스트 - 요청은 한경우 밖에 없기에 다이나믹 테스트 보단 일반 테스트로 진행한다.")
    @Test
    void name() throws JsonProcessingException {
        //given
        String email = "email@email.com";
        String password = "password";
        Member member = MemberAcceptanceHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);

        TokenValuesDto tokenValuesDto = MemberAcceptanceHelper.로그인(email, password).getResult();

        //when
        RestResponse<TokenValidResponse> response = 토큰인증_요청(tokenValuesDto);

        //then
        토큰인증_요청_검증(tokenValuesDto, response);
    }

    private RestResponse<TokenValidResponse> 토큰인증_요청(TokenValuesDto tokenValuesDto) throws JsonProcessingException {
        RestResponse<TokenValidResponse> response = postRequest(TokenControllerPath.VALIDATE_TOKEN_STATUS, TokenValuesDto.of(tokenValuesDto.getAccessToken(), tokenValuesDto.getRefreshToken()),
                new TypeReference<RestResponse<TokenValidResponse>>() {}, "");
        return response;
    }

    private void 토큰인증_요청_검증(TokenValuesDto tokenValuesDto, RestResponse<TokenValidResponse> response) {
        Assertions.assertThat(response.getResult().getAccessToken()).isEqualTo(tokenValuesDto.getAccessToken());
        Assertions.assertThat(response.getResult().isReissuedAccessToken()).isFalse();
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}
