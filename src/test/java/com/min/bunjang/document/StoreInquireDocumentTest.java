package com.min.bunjang.document;

import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.document.config.DocumentTestConfig;
import com.min.bunjang.token.jwt.TokenProvider;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.storeinquire.controller.StoreInquireControllerPath;
import com.min.bunjang.storeinquire.controller.StoreInquireViewControllerPath;
import com.min.bunjang.storeinquire.dto.request.InquireCreateRequest;
import com.min.bunjang.storeinquire.model.StoreInquire;
import com.min.bunjang.storeinquire.repository.StoreInquireRepository;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.Arrays;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StoreInquireDocumentTest extends DocumentTestConfig {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreInquireRepository storeInquireRepository;

    @DisplayName("상점문의 생성 통합테스트")
    @Test
    void storeInquire_create() throws Exception {
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

        InquireCreateRequest inquireCreateRequest = new InquireCreateRequest(owner.getNum(), visitor.getNum(), "상점문의", null);

        //when & then
        mockMvc.perform(post(StoreInquireControllerPath.CREATE_INQUIRY)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, loginResult.getAccessToken())
                        .content(objectMapper.writeValueAsString(inquireCreateRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("storeInquiry-create",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("ownerNum").description("문의받은 상점 식별자 정보 필드."),
                                fieldWithPath("writerNum").description("문의한 상점 식별자 정보 필드."),
                                fieldWithPath("inquireContent").description("문의 내용 정보 필드"),
                                fieldWithPath("mentionedStoreNumForAnswer").description("문의답변을 받아야 하는 문의 작성 상점 식별자 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result.inquireNum").description("응답의 데이터 필드. 등록된 상점문의의 식별자 정보 필드"),
                                fieldWithPath("result.writerName").description("응답의 데이터 필드. 방문상점의 이름 정보 필드"),
                                fieldWithPath("result.inquireContent").description("응답의 데이터 필드. 등록된 문의내용 정보 필드")
                        )
                ));
    }

    @DisplayName("상점문의 목록조회 통합테스트")
    @Test
    void storeInquire_findByOwner() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberAcceptanceHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberAcceptanceHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(writerEmail, writerPassword).getResult();

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);

        storeInquireRepository.saveAll(Arrays.asList(
                StoreInquire.of(owner.getNum(), writer, "content1"),
                StoreInquire.of(owner.getNum(), writer, "content2")
        ));

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get(StoreInquireViewControllerPath.INQUIRIES_FIND_RELATED_STORE, owner.getNum())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("storeInquiry-findByOwner",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        pathParameters(
                                parameterWithName("storeNum").description("상점문의가 달린 상점의 식별자 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        )
                ));
    }

    @DisplayName("상점문의 삭제 통합테스트")
    @Test
    void storeInquire_delete() throws Exception {
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

        StoreInquire storeInquire = StoreInquire.of(owner.getNum(), visitor, "상점문의");
        StoreInquire savedStoreInquiry = storeInquireRepository.save(storeInquire);

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(StoreInquireControllerPath.DELETE_INQUIRY, savedStoreInquiry.getNum())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, loginResult.getAccessToken()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("storeInquiry-delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        pathParameters(
                                parameterWithName("inquireNum").description("삭제하려는 상점문의의 식별자 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("응답의 데이터 필드. ")
                        )
                ));
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}
