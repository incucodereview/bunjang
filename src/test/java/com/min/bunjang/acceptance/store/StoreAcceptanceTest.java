package com.min.bunjang.acceptance.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.acceptance.common.AcceptanceTestConfig;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.controller.StoreControllerPath;
import com.min.bunjang.store.dto.StoreCreateRequest;
import com.min.bunjang.store.dto.StoreCreateResponse;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.stream.Stream;

public class StoreAcceptanceTest extends AcceptanceTestConfig {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @TestFactory
    Stream<DynamicTest> dynamicTestStream() {
        String email = "urisegea@naver.com";
        String password = "password";

        return Stream.of(
                DynamicTest.dynamicTest("상점 생성.", () -> {
                    //given
                    Member member = MemberAcceptanceHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
                    TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(email, password).getResult();

                    String storeName = "storeName";
                    String introduceContent = "introduceContent";
                    StoreCreateRequest storeCreateRequest = new StoreCreateRequest(member.getMemberNum(), storeName, introduceContent);
                    //when
                    StoreCreateResponse storeCreateResponse = 상점생성_요청(loginResult, storeCreateRequest);

                    //then
                    상점생성_요청_검증(storeName, introduceContent, storeCreateResponse);
                })
        );
    }

    private StoreCreateResponse 상점생성_요청(TokenValuesDto loginResult, StoreCreateRequest storeCreateRequest) {
        return postApi(StoreControllerPath.STORE_CREATE, storeCreateRequest, new TypeReference<RestResponse<StoreCreateResponse>>() {
        }, loginResult.getAccessToken()).getResult();
    }

    private void 상점생성_요청_검증(String storeName, String introduceContent, StoreCreateResponse storeCreateResponse) {
        Assertions.assertThat(storeCreateResponse.getStoreId()).isNotNull();
        Assertions.assertThat(storeCreateResponse.getStoreName()).isEqualTo(storeName);
        Assertions.assertThat(storeCreateResponse.getIntroduceContent()).isEqualTo(introduceContent);
    }
}
