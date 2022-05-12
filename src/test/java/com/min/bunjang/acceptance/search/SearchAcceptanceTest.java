package com.min.bunjang.acceptance.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.acceptance.common.AcceptanceTestConfig;
import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.ProductHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.controller.ProductSearchControllerPath;
import com.min.bunjang.product.dto.response.ProductSimpleResponses;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.store.controller.StoreSearchControllerPath;
import com.min.bunjang.store.dto.response.StoreSimpleResponses;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SearchAcceptanceTest extends AcceptanceTestConfig {

    @TestFactory
    Stream<DynamicTest> dynamicTestStream() throws JsonProcessingException {
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.인수테스트_로그인(email, password).getResult();
        Store store = StoreAcceptanceHelper.상점생성(member, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        storeRepository.save(Store.createStore("spring", "intro", null, member));
        storeRepository.save(Store.createStore("spring2", "intro", null, member));
        storeRepository.save(Store.createStore("summer", "intro", null, member));
        storeRepository.save(Store.createStore("fall", "intro", null, member));
        storeRepository.save(Store.createStore("winter", "intro", null, member));

        Product product1 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "하하", "seoul", firstCategory, secondCategory, thirdCategory, productRepository);
        Product product2 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "히히하", "busan", firstCategory, secondCategory, thirdCategory, productRepository);
        Product product3 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "하하히히", "seoul samsung", firstCategory, secondCategory, thirdCategory, productRepository);
        Product product4 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "busan man", "busan", firstCategory, secondCategory, thirdCategory, productRepository);

        return Stream.of(
                DynamicTest.dynamicTest("상품명 키워드 검색.", () -> {
                    //given
                    String keyword = "하하";
                    Map<String, String> parameter = new HashMap<>();
                    parameter.put("keyword", keyword);

                    //when
                    ProductSimpleResponses result = 상품_상품명_검색_요청(loginResult, parameter);

                    //then
                    상품_상품명_검색_응답_검증(product1, product3, result);
                }),

                DynamicTest.dynamicTest("지역명 키워드 검색.", () -> {
                    //given
                    String keyword = "seoul";
                    Map<String, String> parameter = new HashMap<>();
                    parameter.put("keyword", keyword);

                    //when
                    ProductSimpleResponses result = 상품_지역명_검색_요청(loginResult, parameter);

                    //then
                    상품_지역명_검색_응답_검증(product1, product3, result);
                }),

                DynamicTest.dynamicTest("상점명 키워드 검색.", () -> {
                    //given
                    String keyword = "spring";
                    Map<String, String> parameter = new HashMap<>();
                    parameter.put("keyword", keyword);

                    //when
                    StoreSimpleResponses result = 상점명_검색_요청(loginResult, parameter);

                    //then
                    상점명_검색_응답_검증(result);
                })
        );

    }

    private ProductSimpleResponses 상품_상품명_검색_요청(TokenValuesDto loginResult, Map<String, String> parameter) throws JsonProcessingException {
        ProductSimpleResponses result = getRequestWithKeyword(ProductSearchControllerPath.PRODUCT_SEARCH_BY_KEYWORD, loginResult.getAccessToken(), parameter, new TypeReference<RestResponse<ProductSimpleResponses>>() {
        }).getResult();
        return result;
    }

    private void 상품_상품명_검색_응답_검증(Product product1, Product product3, ProductSimpleResponses result) {
        Assertions.assertThat(result.getProductSimpleResponses()).hasSize(2);
        Assertions.assertThat(result.getProductSimpleResponses().get(0).getProductName()).isEqualTo(product3.getProductName());
        Assertions.assertThat(result.getProductSimpleResponses().get(1).getProductName()).isEqualTo(product1.getProductName());
    }

    private ProductSimpleResponses 상품_지역명_검색_요청(TokenValuesDto loginResult, Map<String, String> parameter) throws JsonProcessingException {
        return getRequestWithKeyword(ProductSearchControllerPath.PRODUCT_SEARCH_BY_KEYWORD, loginResult.getAccessToken(), parameter, new TypeReference<RestResponse<ProductSimpleResponses>>() {
        }).getResult();
    }

    private void 상품_지역명_검색_응답_검증(Product product1, Product product3, ProductSimpleResponses result) {
        Assertions.assertThat(result.getProductSimpleResponses()).hasSize(2);
        Assertions.assertThat(result.getProductSimpleResponses().get(0).getExchangeLocation()).isEqualTo(product3.getTradeLocation());
        Assertions.assertThat(result.getProductSimpleResponses().get(1).getExchangeLocation()).isEqualTo(product1.getTradeLocation());
    }

    private StoreSimpleResponses 상점명_검색_요청(TokenValuesDto loginResult, Map<String, String> parameter) throws JsonProcessingException {
        return getRequestWithKeyword(StoreSearchControllerPath.STORE_SEARCH_BY_KEYWORD, loginResult.getAccessToken(), parameter, new TypeReference<RestResponse<StoreSimpleResponses>>() {
                        }).getResult();
    }

    private void 상점명_검색_응답_검증(StoreSimpleResponses result) {
        Assertions.assertThat(result.getStoreSimpleResponses()).hasSize(2);
        Assertions.assertThat(result.getStoreSimpleResponses().get(0).getStoreName()).isEqualTo("spring2");
        Assertions.assertThat(result.getStoreSimpleResponses().get(1).getStoreName()).isEqualTo("spring");
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
