package com.min.bunjang.acceptance.storeinquiry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.acceptance.common.AcceptanceTestConfig;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.storeinquire.controller.StoreInquireControllerPath;
import com.min.bunjang.storeinquire.controller.StoreInquireViewControllerPath;
import com.min.bunjang.storeinquire.dto.InquireCreateRequest;
import com.min.bunjang.storeinquire.dto.InquireCreateResponse;
import com.min.bunjang.storeinquire.dto.StoreInquireResponse;
import com.min.bunjang.storeinquire.dto.StoreInquireListResponses;
import com.min.bunjang.storeinquire.model.StoreInquire;
import com.min.bunjang.storeinquire.repository.StoreInquireRepository;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class StoreInquireAcceptanceTest extends AcceptanceTestConfig {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreInquireRepository storeInquiryRepository;

    @TestFactory
    Stream<DynamicTest> dynamicTestStream() {
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberAcceptanceHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberAcceptanceHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(writerEmail, writerPassword).getResult();

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);

        return Stream.of(
                DynamicTest.dynamicTest("상점문의 생성.", () -> {
                    //given
                    String inquiryContent = "인수테스트 상점 문의 내용";

                    InquireCreateRequest inquireCreateRequest = new InquireCreateRequest(owner.getNum(), writer.getNum(), inquiryContent, null);

                    //when
                    InquireCreateResponse inquireCreateResponse = 상점문의생성_요청(loginResult, inquireCreateRequest);

                    //then
                    상점문의생성_응답_검증(writer, inquiryContent, inquireCreateResponse);
                }),

                DynamicTest.dynamicTest("상점문의 목록 조회.", () -> {
                    //given
                    Long storeNum = owner.getNum();
                    PageRequest pageRequest = PageRequest.of(0, 10);

                    //when
                    StoreInquireListResponses storeInquireListResponses = 상점문의_목록조회_요청(loginResult, storeNum);

                    //then
                    상점문의_목록조회_응답_검증(storeInquireListResponses);
                }),

                DynamicTest.dynamicTest("상점문의 삭제.", () -> {
                    //given
                    List<StoreInquire> storeInquires = storeInquiryRepository.findAll();
                    StoreInquire storeInquire = storeInquires.get(0);
                    Long storeInquireNum = storeInquire.getNum();

                    //when
                    상점문의삭제_요청(loginResult, storeInquireNum);

                    //then
                    상점문의삭제_응답_검증(storeInquireNum);
                })
        );
    }

    private InquireCreateResponse 상점문의생성_요청(TokenValuesDto loginResult, InquireCreateRequest inquireCreateRequest) {
        InquireCreateResponse inquireCreateResponse = postApi(StoreInquireControllerPath.CREATE_INQUIRY, inquireCreateRequest, new TypeReference<RestResponse<InquireCreateResponse>>() {
        }, loginResult.getAccessToken()).getResult();
        return inquireCreateResponse;
    }

    private void 상점문의생성_응답_검증(Store writer, String inquiryContent, InquireCreateResponse inquireCreateResponse) {
        Assertions.assertThat(inquireCreateResponse.getWriterName()).isEqualTo(writer.getStoreName());
        Assertions.assertThat(inquireCreateResponse.getInquireContent()).isEqualTo(inquiryContent);
    }

    private void 상점문의삭제_요청(TokenValuesDto loginResult, Long storeInquireNum) {
        String path = StoreInquireControllerPath.DELETE_INQUIRY.replace("{inquireNum}", String.valueOf(storeInquireNum));
        deleteApi(path, null, new TypeReference<RestResponse<RestResponse<Void>>>() {}, loginResult.getAccessToken());
    }

    private void 상점문의삭제_응답_검증(Long storeInquireNum) {
        Optional<StoreInquire> findStoreInquire = storeInquiryRepository.findById(storeInquireNum);
        Assertions.assertThat(findStoreInquire.isPresent()).isFalse();
    }

    private StoreInquireListResponses 상점문의_목록조회_요청(TokenValuesDto loginResult, Long storeNum) {
        String path = StoreInquireViewControllerPath.INQUIRIES_FIND_RELATED_STORE.replace("{storeNum}", String.valueOf(storeNum));
        StoreInquireListResponses storeInquireListResponses = getApi(path, loginResult.getAccessToken(), new TypeReference<RestResponse<StoreInquireListResponses>>() {
        }).getResult();
        return storeInquireListResponses;
    }

    private void 상점문의_목록조회_응답_검증(StoreInquireListResponses storeInquireListResponses) {
        List<StoreInquireResponse> storeInquireResponse = storeInquireListResponses.getStoreInquireResponse();
        Assertions.assertThat(storeInquireResponse).hasSize(1);
        Assertions.assertThat(storeInquireResponse.get(0).getInquireContent()).isEqualTo("인수테스트 상점 문의 내용");
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}
