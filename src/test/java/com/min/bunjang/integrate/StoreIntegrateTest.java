package com.min.bunjang.integrate;

import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.integrate.config.IntegrateTestConfig;
import com.min.bunjang.login.jwt.TokenProvider;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.controller.StoreControllerPath;
import com.min.bunjang.store.dto.StoreCreateRequest;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StoreIntegrateTest extends IntegrateTestConfig {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("상점 생성 통합테스트")
    @Test
    void store_create() throws Exception {
        //given
        String email = "email";
        String password = "password";
        Member member = MemberAcceptanceHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(email, password).getResult();

        String storeName = "storeName";
        String introduceContent = "introduceContent";

        StoreCreateRequest storeCreateRequest = new StoreCreateRequest(member.getMemberNum(), storeName, introduceContent);

        //when & then
        mockMvc.perform(post(StoreControllerPath.STORE_CREATE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TokenProvider.ACCESS_TOKEN_KEY_OF_HEADER, loginResult.getAccessToken())
                .content(objectMapper.writeValueAsString(storeCreateRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("store-create",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("memberId").description("상점을 생성하는 회원의 식별자"),
                                fieldWithPath("storeName").description("상점명 요청 필드"),
                                fieldWithPath("introduceContent").description("상점 소개글 요청 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result.storeId").description("생성된 상점의 식별자 정보 필드"),
                                fieldWithPath("result.storeName").description("생성된 상점의 이름 정보 필드"),
                                fieldWithPath("result.introduceContent").description("생성된 상점의 소개글 정보 필드")
                        )
                        ));
    }
}
