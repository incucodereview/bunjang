package com.min.bunjang.acceptance.storeinquiry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.acceptance.common.AcceptanceTestConfig;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.store.dto.StoreCreateRequest;
import com.min.bunjang.store.dto.StoreCreateResponse;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.store.storeinquiry.controller.StoreInquiryControllerPath;
import com.min.bunjang.store.storeinquiry.dto.InquiryCreateRequest;
import com.min.bunjang.store.storeinquiry.dto.InquiryCreateResponse;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

public class StoreInquiryAcceptanceTest extends AcceptanceTestConfig {
    @Autowired
    private StoreRepository storeRepository;

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

                    InquiryCreateRequest inquiryCreateRequest = new InquiryCreateRequest(owner.getNum(), writer.getNum(), inquiryContent);
                    //when
                    InquiryCreateResponse inquiryCreateResponse = postApi(StoreInquiryControllerPath.CREATE_INQUIRY, inquiryCreateRequest, new TypeReference<RestResponse<InquiryCreateResponse>>() {
                    }, loginResult.getAccessToken()).getResult();

                    //then
                    Assertions.assertThat(inquiryCreateResponse.getWriterName()).isEqualTo(writer.getStoreName());
                    Assertions.assertThat(inquiryCreateResponse.getInquiryContent()).isEqualTo(inquiryContent);

                })
        );
    }
}
