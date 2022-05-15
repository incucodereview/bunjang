package com.min.bunjang.integrate;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.config.IntegrateBaseTest;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.ProductHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.controller.ProductSearchControllerPath;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.store.controller.StoreSearchControllerPath;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class SearchIntegrateTest extends IntegrateBaseTest {

    @DisplayName("상품명 키워드 검색 통합테스트")
    @Test
    public void 상품명_검색() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(email, password, loginService);
        Store store = StoreAcceptanceHelper.상점생성(member, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        storeRepository.save(Store.createStore("spring", "intro", null, member));
        storeRepository.save(Store.createStore("spring2", "intro", null, member));
        storeRepository.save(Store.createStore("summer", "intro", null, member));
        storeRepository.save(Store.createStore("fall", "intro", null, member));
        storeRepository.save(Store.createStore("winter", "intro", null, member));

        Product product1 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "조던1", "seoul", firstCategory, secondCategory, thirdCategory, productRepository);
        Product product2 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "조던2", "busan", firstCategory, secondCategory, thirdCategory, productRepository);
        Product product3 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "에조던어", "seoul samsung", firstCategory, secondCategory, thirdCategory, productRepository);
        Product product4 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "저어던에", "busan", firstCategory, secondCategory, thirdCategory, productRepository);

        //when & then
        ResultActions resultActions = getRequestWithParam(loginResult.getAccessToken(), ProductSearchControllerPath.PRODUCT_SEARCH_BY_KEYWORD, "keyword", "조던");
        resultActions.andExpect(jsonPath("result.productSimpleResponses.[0]").isNotEmpty());
        resultActions.andExpect(jsonPath("result.productSimpleResponses.[1]").isNotEmpty());
        resultActions.andExpect(jsonPath("result.productSimpleResponses.[2]").isNotEmpty());
        resultActions.andExpect(jsonPath("result.productSimpleResponses.[3]").doesNotExist());
    }

    @DisplayName("지역명 키워드 검색 통합테스트")
    @Test
    public void 지역명_검색() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(email, password, loginService);
        Store store = StoreAcceptanceHelper.상점생성(member, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        storeRepository.save(Store.createStore("spring", "intro", null, member));
        storeRepository.save(Store.createStore("spring2", "intro", null, member));
        storeRepository.save(Store.createStore("summer", "intro", null, member));
        storeRepository.save(Store.createStore("fall", "intro", null, member));
        storeRepository.save(Store.createStore("winter", "intro", null, member));

        Product product1 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "조던1", "seoul", firstCategory, secondCategory, thirdCategory, productRepository);
        Product product2 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "조던2", "busan", firstCategory, secondCategory, thirdCategory, productRepository);
        Product product3 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "에조던어", "seoul samsung", firstCategory, secondCategory, thirdCategory, productRepository);
        Product product4 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "저어던에", "busan", firstCategory, secondCategory, thirdCategory, productRepository);

        //when & then
        ResultActions resultActions = getRequestWithParam(loginResult.getAccessToken(), ProductSearchControllerPath.PRODUCT_SEARCH_BY_KEYWORD, "keyword", "seoul");
        resultActions.andExpect(jsonPath("result.productSimpleResponses.[0]").isNotEmpty());
        resultActions.andExpect(jsonPath("result.productSimpleResponses.[1]").isNotEmpty());
        resultActions.andExpect(jsonPath("result.productSimpleResponses.[2]").doesNotExist());
    }

    @DisplayName("상점명 키워드 검색 통합테스트")
    @Test
    public void 상점명_검색() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(email, password, loginService);
        Store store = StoreAcceptanceHelper.상점생성(member, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        storeRepository.save(Store.createStore("spring", "intro", null, member));
        storeRepository.save(Store.createStore("spring2", "intro", null, member));
        storeRepository.save(Store.createStore("summer", "intro", null, member));
        storeRepository.save(Store.createStore("fall", "intro", null, member));
        storeRepository.save(Store.createStore("winter", "intro", null, member));

        Product product1 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "조던1", "seoul", firstCategory, secondCategory, thirdCategory, productRepository);
        Product product2 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "조던2", "busan", firstCategory, secondCategory, thirdCategory, productRepository);
        Product product3 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "에조던어", "seoul samsung", firstCategory, secondCategory, thirdCategory, productRepository);
        Product product4 = ProductHelper.상품생성_상품이름_거래지역_적용(store, "저어던에", "busan", firstCategory, secondCategory, thirdCategory, productRepository);


        //when & then
        ResultActions resultActions = getRequestWithParam(loginResult.getAccessToken(), StoreSearchControllerPath.STORE_SEARCH_BY_KEYWORD, "keyword", "spring");
        resultActions.andExpect(jsonPath("result.storeSimpleResponses.[0]").isNotEmpty());
        resultActions.andExpect(jsonPath("result.storeSimpleResponses.[1]").isNotEmpty());
        resultActions.andExpect(jsonPath("result.storeSimpleResponses.[2]").doesNotExist());
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
