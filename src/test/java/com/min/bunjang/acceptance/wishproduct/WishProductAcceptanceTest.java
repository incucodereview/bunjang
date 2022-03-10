package com.min.bunjang.acceptance.wishproduct;

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
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.wishproduct.controller.WishProductControllerPath;
import com.min.bunjang.wishproduct.controller.WishProductViewControllerPath;
import com.min.bunjang.wishproduct.dto.WishProductCreateRequest;
import com.min.bunjang.wishproduct.dto.WishProductResponses;
import com.min.bunjang.wishproduct.dto.WishProductsDeleteRequest;
import com.min.bunjang.wishproduct.model.WishProduct;
import com.min.bunjang.wishproduct.repository.WishProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class WishProductAcceptanceTest extends AcceptanceTestConfig {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishProductRepository wishProductRepository;

    // ERROR
    @TestFactory
    Stream<DynamicTest> dynamicTestStream() {
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberAcceptanceHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberAcceptanceHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(ownerEmail, ownerPassword).getResult();

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);
        //TODO 임시 생성자로 생성해놓음.
        Product product = productRepository.save(new Product("productName"));

        return Stream.of(
                DynamicTest.dynamicTest("찜목록에 찜상품 생성(추가).", () -> {
                    //given
                    WishProductCreateRequest wishProductCreateRequest = new WishProductCreateRequest(owner.getNum(), product.getNum());

                    //when
                    찜상품_생성_요청(loginResult, wishProductCreateRequest);

                    //then
                    찜상품_생성_응답_검증();
                }),

                DynamicTest.dynamicTest("상점의 찜목록 조회 ", () -> {
                    //given
                    Long storeNum = owner.getNum();
                    PageRequest pageRequest = PageRequest.of(0, 10);

                    //when
                    찜목록_조회_요청(loginResult, storeNum);

                    //then
                    찜목록_조회_응답_검증();
                }),

                DynamicTest.dynamicTest("찜목록에서 찜상품들 삭제", () -> {
                    //given
                    WishProduct wishProduct = wishProductRepository.findAll().get(0);
                    WishProductsDeleteRequest wishProductsDeleteRequest = new WishProductsDeleteRequest(Arrays.asList(wishProduct.getNum()), owner.getNum());

                    //when
                    찜상품_삭제_요청(loginResult, wishProductsDeleteRequest);

                    //then
                    찜상풍_삭제_응답_검증();
                })
        );
    }

    private void 찜목록_조회_응답_검증() {
        List<WishProduct> wishProducts = wishProductRepository.findAll();
        Assertions.assertThat(wishProducts).hasSize(1);
    }

    private void 찜목록_조회_요청(TokenValuesDto loginResult, Long storeNum) {
        String path = WishProductViewControllerPath.WISH_PRODUCT_FIND_BY_STORE.replace("{storeNum}", String.valueOf(storeNum));
        getApi(path, loginResult.getAccessToken(), new TypeReference<RestResponse<WishProductResponses>>() {});
    }

    private void 찜상품_생성_요청(TokenValuesDto loginResult, WishProductCreateRequest wishProductCreateRequest) {
        postApi(WishProductControllerPath.WISH_PRODUCT_CREATE, wishProductCreateRequest, new TypeReference<RestResponse<Void>>() {}, loginResult.getAccessToken());
    }

    private void 찜상품_생성_응답_검증() {
        WishProduct wishProduct = wishProductRepository.findAll().get(0);
        Assertions.assertThat(wishProduct).isNotNull();
    }

    private void 찜상품_삭제_요청(TokenValuesDto loginResult, WishProductsDeleteRequest wishProductsDeleteRequest) {
        deleteApi(WishProductControllerPath.WISH_PRODUCT_DELETE, wishProductsDeleteRequest, new TypeReference<RestResponse<Void>>() {}, loginResult.getAccessToken());
    }

    private void 찜상풍_삭제_응답_검증() {
        List<WishProduct> wishProducts = wishProductRepository.findAll();
        Assertions.assertThat(wishProducts).isEmpty();
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}
