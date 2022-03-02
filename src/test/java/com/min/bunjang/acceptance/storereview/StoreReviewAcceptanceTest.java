package com.min.bunjang.acceptance.storereview;

import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.acceptance.common.AcceptanceTestConfig;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.storereview.controller.StoreReviewControllerPath;
import com.min.bunjang.storereview.dto.StoreReviewCreateRequest;
import com.min.bunjang.storereview.dto.StoreReviewResponse;
import com.min.bunjang.storereview.dto.StoreReviewUpdateRequest;
import com.min.bunjang.storereview.model.StoreReview;
import com.min.bunjang.storereview.repository.StoreReviewRepository;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.stream.Stream;

public class StoreReviewAcceptanceTest extends AcceptanceTestConfig {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreReviewRepository storeReviewRepository;

    @Autowired
    private ProductRepository productRepository;

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
        //TODO 임시 생성자로 생성해놓음.
        Product product = productRepository.save(new Product("productName"));

        return Stream.of(
                DynamicTest.dynamicTest("상점후기 생성.", () -> {
                    //given
                    double dealScore = 4.5;
                    String reviewContent = "reviewContent";

                    StoreReviewCreateRequest storeReviewCreateRequest = new StoreReviewCreateRequest(
                            owner.getNum(),
                            writer.getNum(),
                            dealScore,
                            product.getNum(),
                            product.getProductName(),
                            reviewContent
                    );

                    //when
                    StoreReviewResponse storeReviewResponse = 상점후기_생성_요청(loginResult, storeReviewCreateRequest);

                    //then
                    상점후기_생성_응답_검증(writer, product, dealScore, reviewContent, storeReviewResponse);
                }),

                DynamicTest.dynamicTest("상점후기 변경.", () -> {
                    //given
                    StoreReview storeReview = storeReviewRepository.findAll().get(0);

                    double dealScore = 4.5;
                    String updatedReviewContent = "updatedReviewContent";

                    StoreReviewUpdateRequest storeReviewUpdateRequest = new StoreReviewUpdateRequest(storeReview.getNum(), dealScore, updatedReviewContent);

                    //when
                    상점후기_변경_요청(loginResult, storeReviewUpdateRequest);

                    //then
                    상점후기_변경_응답_검증(storeReview.getNum(), dealScore, updatedReviewContent);
                }),

                DynamicTest.dynamicTest("상점후기 삭제.", () -> {
                    //given
                    StoreReview storeReview = storeReviewRepository.findAll().get(0);

                    //when
                    상점후기_삭제_요청(loginResult, storeReview);

                    //then
                    상점후기_삭제_응답_검증(storeReview);
                })

        );
    }

    private StoreReviewResponse 상점후기_생성_요청(TokenValuesDto loginResult, StoreReviewCreateRequest storeReviewCreateRequest) {
        return postApi(StoreReviewControllerPath.REVIEW_CREATE, storeReviewCreateRequest, new TypeReference<RestResponse<StoreReviewResponse>>() {
        }, loginResult.getAccessToken()).getResult();
    }

    private void 상점후기_생성_응답_검증(Store writer, Product product, double dealScore, String reviewContent, StoreReviewResponse storeReviewResponse) {
        Assertions.assertThat(storeReviewResponse.getWriterNum()).isEqualTo(writer.getNum());
        Assertions.assertThat(storeReviewResponse.getWriterName()).isEqualTo(writer.getStoreName());
        Assertions.assertThat(storeReviewResponse.getDealScore()).isEqualTo(dealScore);
        Assertions.assertThat(storeReviewResponse.getProductNum()).isEqualTo(product.getNum());
        Assertions.assertThat(storeReviewResponse.getProductName()).isEqualTo(product.getProductName());
        Assertions.assertThat(storeReviewResponse.getReviewContent()).isEqualTo(reviewContent);
    }

    private RestResponse<Void> 상점후기_변경_요청(TokenValuesDto loginResult, StoreReviewUpdateRequest storeReviewUpdateRequest) {
        return putApi(StoreReviewControllerPath.REVIEW_UPDATE, storeReviewUpdateRequest, new TypeReference<RestResponse<Void>>() {
        }, loginResult.getAccessToken());
    }

    private void 상점후기_변경_응답_검증(Long reviewNum, double dealScore, String updateStoreReview) {
        StoreReview updatedStoreReview = storeReviewRepository.findById(reviewNum).get();
        Assertions.assertThat(updatedStoreReview.getDealScore()).isEqualTo(dealScore);
        Assertions.assertThat(updatedStoreReview.getReviewContent()).isEqualTo(updateStoreReview);
    }

    private void 상점후기_삭제_요청(TokenValuesDto loginResult, StoreReview storeReview) {
        String path = StoreReviewControllerPath.REVIEW_DELETE.replace("{reviewNum}", String.valueOf(storeReview.getNum()));
        deleteApi(path, null, new TypeReference<RestResponse<Void>>() {
        }, loginResult.getAccessToken());
    }

    private void 상점후기_삭제_응답_검증(StoreReview storeReview) {
        Optional<StoreReview> deletedStoreReview = storeReviewRepository.findById(storeReview.getNum());
        Assertions.assertThat(deletedStoreReview.isPresent()).isFalse();
    }

}
