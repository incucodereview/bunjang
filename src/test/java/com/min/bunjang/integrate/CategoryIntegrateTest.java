package com.min.bunjang.integrate;

import com.min.bunjang.category.controller.CategoryViewControllerPath;
import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.config.IntegrateBaseTest;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.ProductHelper;
import com.min.bunjang.helpers.StoreHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.store.model.Store;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryIntegrateTest extends IntegrateBaseTest {

    @DisplayName("모든 카테고리 조회 통합테스트")
    @Test
    public void 모든카테고리_조회() throws Exception {
        //given
        FirstProductCategory category1 = FirstProductCategory.createFirstProductCategory("category1");
        SecondProductCategory category2 = SecondProductCategory.createSecondCategory("category2", category1);
        ThirdProductCategory category3 = ThirdProductCategory.createThirdCategory("category3", category2);
        FirstProductCategory category4 = FirstProductCategory.createFirstProductCategory("category4");

        firstProductCategoryRepository.save(category1);
        firstProductCategoryRepository.save(category4);
        secondProductCategoryRepository.save(category2);
        thirdProductCategoryRepository.save(category3);

        //when && then
        mockMvc.perform(get(CategoryViewControllerPath.CATEGORY_FIND_ALL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("result.firstProductCategoryResponseList").isNotEmpty())
                .andDo(print());
    }

    @DisplayName("first 카테고리의 상품 조회 통합테스트")
    @Test
    public void FIRST_카테고리_상품_조회() throws Exception {
        //given
        FirstProductCategory category1 = FirstProductCategory.createFirstProductCategory("category1");
        SecondProductCategory category2 = SecondProductCategory.createSecondCategory("category2", category1);
        ThirdProductCategory category3 = ThirdProductCategory.createThirdCategory("category3", category2);

        FirstProductCategory saveFirstCate = firstProductCategoryRepository.save(category1);
        SecondProductCategory saveSecondCate = secondProductCategoryRepository.save(category2);
        ThirdProductCategory saveThirdCate = thirdProductCategoryRepository.save(category3);

        Member member = MemberHelper.회원가입("email", "password", memberRepository, bCryptPasswordEncoder);
        Store store = StoreHelper.상점생성(member, storeRepository);
        Product product1 = ProductHelper.상품생성(store, saveFirstCate, saveSecondCate, saveThirdCate, productRepository);

        //when && then
        mockMvc.perform(RestDocumentationRequestBuilders.get(CategoryViewControllerPath.CATEGORY_FIND_BY_FIRST, saveFirstCate.getNum())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("result.productSimpleResponses").isNotEmpty())
                .andDo(print());
    }

    @DisplayName("second 카테고리의 상품 조회 통합테스트")
    @Test
    public void SECOND_카테고리_상품_조회() throws Exception {
        //given
        FirstProductCategory category1 = FirstProductCategory.createFirstProductCategory("category1");
        SecondProductCategory category2 = SecondProductCategory.createSecondCategory("category2", category1);
        ThirdProductCategory category3 = ThirdProductCategory.createThirdCategory("category3", category2);

        FirstProductCategory saveFirstCate = firstProductCategoryRepository.save(category1);
        SecondProductCategory saveSecondCate = secondProductCategoryRepository.save(category2);
        ThirdProductCategory saveThirdCate = thirdProductCategoryRepository.save(category3);

        Member member = MemberHelper.회원가입("email", "password", memberRepository, bCryptPasswordEncoder);
        Store store = StoreHelper.상점생성(member, storeRepository);
        Product product1 = ProductHelper.상품생성(store, saveFirstCate, saveSecondCate, saveThirdCate, productRepository);

        //when && then
        mockMvc.perform(RestDocumentationRequestBuilders.get(CategoryViewControllerPath.CATEGORY_FIND_BY_SECOND, saveSecondCate.getNum())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("result.productSimpleResponses").isNotEmpty())
                .andDo(print());
    }

    @DisplayName("third 카테고리의 상품 조회 통합테스트")
    @Test
    public void THIRD_카테고리_상품_조회() throws Exception {
        //given
        FirstProductCategory category1 = FirstProductCategory.createFirstProductCategory("category1");
        SecondProductCategory category2 = SecondProductCategory.createSecondCategory("category2", category1);
        ThirdProductCategory category3 = ThirdProductCategory.createThirdCategory("category3", category2);

        FirstProductCategory saveFirstCate = firstProductCategoryRepository.save(category1);
        SecondProductCategory saveSecondCate = secondProductCategoryRepository.save(category2);
        ThirdProductCategory saveThirdCate = thirdProductCategoryRepository.save(category3);

        Member member = MemberHelper.회원가입("email", "password", memberRepository, bCryptPasswordEncoder);
        Store store = StoreHelper.상점생성(member, storeRepository);
        Product product1 = ProductHelper.상품생성(store, saveFirstCate, saveSecondCate, saveThirdCate, productRepository);

        //when && then
        mockMvc.perform(RestDocumentationRequestBuilders.get(CategoryViewControllerPath.CATEGORY_FIND_BY_THIRD, saveSecondCate.getNum())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("result.productSimpleResponses").isNotEmpty())
                .andDo(print());
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
