package com.min.bunjang.acceptance.search;

import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.acceptance.common.AcceptanceTestConfig;
import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.helpers.ProductHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.controller.ProductSearchControllerPath;
import com.min.bunjang.product.dto.ProductSimpleResponses;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SearchAcceptanceTest extends AcceptanceTestConfig {

    @TestFactory
    Stream<DynamicTest> dynamicTestStream() {
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberAcceptanceHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(email, password).getResult();
        Store store = StoreAcceptanceHelper.상점생성(member, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        Product produc1 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "하하", "seoul", firstCategory, secondCategory, thirdCategory, productRepository);
        Product produc2 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "히히하", "busan", firstCategory, secondCategory, thirdCategory, productRepository);
        Product produc3 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "하하히히", "seoul samsung", firstCategory, secondCategory, thirdCategory, productRepository);
        Product produc4 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "seoul man", "busan", firstCategory, secondCategory, thirdCategory, productRepository);

        return Stream.of(
                DynamicTest.dynamicTest("상품명 키워드 검색.", () -> {
                    //given
                    String keyword = "하하";
                    Map<String, String> parameter = new HashMap<>();
                    parameter.put("keyword", keyword);

                    //when
                    ProductSimpleResponses result = getApiWithKeyword(ProductSearchControllerPath.PRODUCT_SEARCH_BY_KEYWORD, loginResult.getAccessToken(), parameter, new TypeReference<RestResponse<ProductSimpleResponses>>() {
                    }).getResult();

                    //then
                    Assertions.assertThat(result.getProductSimpleResponses()).hasSize(2);
                }),

                DynamicTest.dynamicTest("지역명 키워드 검색.", () -> {
                    //given
                    String keyword = "seoul";
                    Map<String, String> parameter = new HashMap<>();
                    parameter.put("keyword", keyword);

                    //when
                    ProductSimpleResponses result = getApiWithKeyword(ProductSearchControllerPath.PRODUCT_SEARCH_BY_KEYWORD, loginResult.getAccessToken(), parameter, new TypeReference<RestResponse<ProductSimpleResponses>>() {
                    }).getResult();

                    //then
                    Assertions.assertThat(result.getProductSimpleResponses()).hasSize(3);
                })
        );

    }
}
