package com.min.bunjang.integrate;

import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.integrate.config.IntegrateTestConfig;
import com.min.bunjang.login.jwt.TokenProvider;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.store.controller.StoreControllerPath;
import com.min.bunjang.store.dto.request.StoreCreateOrUpdateRequest;
import com.min.bunjang.store.dto.request.StoreIntroduceUpdateRequest;
import com.min.bunjang.store.dto.request.StoreNameUpdateRequest;
import com.min.bunjang.store.dto.request.VisitorPlusDto;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StoreIntegrateTest extends IntegrateTestConfig {
    @Autowired
    private StoreRepository storeRepository;

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

        StoreCreateOrUpdateRequest storeCreateOrUpdateRequest = new StoreCreateOrUpdateRequest(storeName, introduceContent, null, null, null, null);

        //when & then
        mockMvc.perform(post(StoreControllerPath.STORE_CREATE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_OF_HEADER, loginResult.getAccessToken())
                        .content(objectMapper.writeValueAsString(storeCreateOrUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("store-create",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
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
                                fieldWithPath("result.introduceContent").description("생성된 상점의 소개글 정보 필드"),
                                fieldWithPath("result.storeThumbnail").description("생성된 상점의 섬네일 정보 필드"),
                                fieldWithPath("result.contactableTime").description("생성된 상점의 응답시간 정보 필드"),
                                fieldWithPath("result.exchangeAndReturnAndRefundPolicy").description("생성된 상점의 교환/환불/반품 주의사항 정보 필드"),
                                fieldWithPath("result.cautionNoteBeforeTrade").description("생성된 상점의 상품 구매전 주의사항 정보 필드")
                        )
                ));
    }

    @DisplayName("상점 소개글 변경 통합테스트")
    @Test
    void store_introduceContent_update() throws Exception {
        //given
        String email = "email1";
        String password = "password";
        Member member = MemberAcceptanceHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(email, password).getResult();

        Store store = StoreAcceptanceHelper.상점생성(member, storeRepository);
        String updateIntroduceContent = "updateIntroduceContent";

        StoreIntroduceUpdateRequest storeIntroduceUpdateRequest = new StoreIntroduceUpdateRequest(updateIntroduceContent);

        //when & then
        mockMvc.perform(put(StoreControllerPath.STORE_INTRODUCE_CONTENT_UPDATE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_OF_HEADER, loginResult.getAccessToken())
                        .content(objectMapper.writeValueAsString(storeIntroduceUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("store-introduceContent-update",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("updateIntroduceContent").description("변경될 소개글 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("응답의 데이터 필드")
                        )
                ));
    }

    @DisplayName("상점이름 변경 통합테스트")
    @Test
    void store_name_update() throws Exception {
        //given
        String email = "email";
        String password = "password";
        Member member = MemberAcceptanceHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(email, password).getResult();

        Store store = StoreAcceptanceHelper.상점생성(member, storeRepository);
        String updateStoreName = "updateStoreName";

        StoreNameUpdateRequest storeNameUpdateRequest = new StoreNameUpdateRequest(updateStoreName);

        //when & then
        mockMvc.perform(put(StoreControllerPath.STORE_NAME_UPDATE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_OF_HEADER, loginResult.getAccessToken())
                        .content(objectMapper.writeValueAsString(storeNameUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("store-name-update",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("updatedStoreName").description("변경될 상점이름 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("응답의 데이터 필드")
                        )
                ));
    }

    @DisplayName("상점 방문자 카운트 통합테스트")
    @Test
    void store_plusVisitor() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberAcceptanceHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String visitorEmail = "visitor@naver.com";
        String visitorPassword = "password!visitor";
        Member visitorMember = MemberAcceptanceHelper.회원가입(visitorEmail, visitorPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(visitorEmail, visitorPassword).getResult();

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store visitor = StoreAcceptanceHelper.상점생성(visitorMember, storeRepository);

        VisitorPlusDto visitorPlusDto = new VisitorPlusDto(owner.getNum());

        //when & then
        mockMvc.perform(post(StoreControllerPath.STORE_PLUS_VISITOR)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_OF_HEADER, loginResult.getAccessToken())
                        .content(objectMapper.writeValueAsString(visitorPlusDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("store-plusVisitor",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("ownerNum").description("소개글이 변경될 상점의 식별자 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("응답의 데이터 필드")
                        )
                ));
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}
