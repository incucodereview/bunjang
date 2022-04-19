package com.min.bunjang.document;

import com.min.bunjang.category.controller.CategoryViewControllerPath;
import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.category.repository.SecondProductCategoryRepository;
import com.min.bunjang.category.repository.ThirdProductCategoryRepository;
import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.helpers.ProductHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.document.config.DocumentTestConfig;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.store.model.Store;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryDocumentTest extends DocumentTestConfig {

    @Autowired
    private FirstProductCategoryRepository firstProductCategoryRepository;

    @Autowired
    private SecondProductCategoryRepository secondProductCategoryRepository;

    @Autowired
    private ThirdProductCategoryRepository thirdProductCategoryRepository;

    @DisplayName("모든 카테고리 조회 통합테스트")
    @Test
    public void category_all_find() throws Exception {
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
                .andDo(print())
                .andDo(document("category-find-all",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("응답의 데이터 필드."),
                                fieldWithPath("result.firstProductCategoryResponseList").description("first 카테고리 데이터"),
                                fieldWithPath("result.firstProductCategoryResponseList[0].categoryNum").description("first 카테고리 식별자 필드"),
                                fieldWithPath("result.firstProductCategoryResponseList[0].categoryName").description("first 카테고리명 필드"),
                                fieldWithPath("result.firstProductCategoryResponseList[0].secondProductCategoryResponseList").description("second 카테고리 데이터"),
                                fieldWithPath("result.firstProductCategoryResponseList[0].secondProductCategoryResponseList[0].categoryNum").description("second 카테고리 식별자 필드"),
                                fieldWithPath("result.firstProductCategoryResponseList[0].secondProductCategoryResponseList[0].categoryName").description("second 카테고리명 필드"),
                                fieldWithPath("result.firstProductCategoryResponseList[0].secondProductCategoryResponseList[0].thirdProductCategoryResponses").description("third 카테고리 데이터"),
                                fieldWithPath("result.firstProductCategoryResponseList[0].secondProductCategoryResponseList[0].thirdProductCategoryResponses[0].categoryNum").description("third 카테고리 식별자 필드"),
                                fieldWithPath("result.firstProductCategoryResponseList[0].secondProductCategoryResponseList[0].thirdProductCategoryResponses[0].categoryName").description("third 카테고리명 필드")
                        )
                ));
    }

    @DisplayName("first 카테고리의 상품 조회 통합테스트")
    @Test
    public void category_find_by_firstCategory() throws Exception {
        //given
        FirstProductCategory category1 = FirstProductCategory.createFirstProductCategory("category1");
        SecondProductCategory category2 = SecondProductCategory.createSecondCategory("category2", category1);
        ThirdProductCategory category3 = ThirdProductCategory.createThirdCategory("category3", category2);

        FirstProductCategory saveFirstCate = firstProductCategoryRepository.save(category1);
        SecondProductCategory saveSecondCate = secondProductCategoryRepository.save(category2);
        ThirdProductCategory saveThirdCate = thirdProductCategoryRepository.save(category3);

        Member member = MemberAcceptanceHelper.회원가입("email", "password", memberRepository, bCryptPasswordEncoder);
        Store store = StoreAcceptanceHelper.상점생성(member, storeRepository);
        Product product1 = ProductHelper.상품생성(store, saveFirstCate, saveSecondCate, saveThirdCate, productRepository);

        //when && then
        mockMvc.perform(RestDocumentationRequestBuilders.get(CategoryViewControllerPath.CATEGORY_FIND_BY_FIRST, saveFirstCate.getNum())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(status().isOk())
                .andDo(print())
                .andDo(document("category-find-by-firstCategory",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        pathParameters(
                                parameterWithName("firstCategoryNum").description("first 카테고리 식별자 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("응답의 데이터 필드."),
                                subsectionWithPath("result.productSimpleResponses").description("first 카테고리에 해당하는 상품 리스트 데이터 필드."),
                                subsectionWithPath("result.pageDto").description("페이지네이션 관련 데이터 필드.")
                        )
                ));
    }

    @DisplayName("second 카테고리의 상품 조회 통합테스트")
    @Test
    public void category_find_by_secondCategory() throws Exception {
        //given
        FirstProductCategory category1 = FirstProductCategory.createFirstProductCategory("category1");
        SecondProductCategory category2 = SecondProductCategory.createSecondCategory("category2", category1);
        ThirdProductCategory category3 = ThirdProductCategory.createThirdCategory("category3", category2);

        FirstProductCategory saveFirstCate = firstProductCategoryRepository.save(category1);
        SecondProductCategory saveSecondCate = secondProductCategoryRepository.save(category2);
        ThirdProductCategory saveThirdCate = thirdProductCategoryRepository.save(category3);

        Member member = MemberAcceptanceHelper.회원가입("email", "password", memberRepository, bCryptPasswordEncoder);
        Store store = StoreAcceptanceHelper.상점생성(member, storeRepository);
        Product product1 = ProductHelper.상품생성(store, saveFirstCate, saveSecondCate, saveThirdCate, productRepository);

        //when && then
        mockMvc.perform(RestDocumentationRequestBuilders.get(CategoryViewControllerPath.CATEGORY_FIND_BY_SECOND, saveSecondCate.getNum())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(status().isOk())
                .andDo(print())
                .andDo(document("category-find-by-secondCategory",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        pathParameters(
                                parameterWithName("secondCategoryNum").description("second 카테고리 식별자 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("응답의 데이터 필드."),
                                subsectionWithPath("result.productSimpleResponses").description("second 카테고리에 해당하는 상품 리스트 데이터 필드."),
                                subsectionWithPath("result.pageDto").description("페이지네이션 관련 데이터 필드.")
                        )
                ));
    }

    @DisplayName("third 카테고리의 상품 조회 통합테스트")
    @Test
    public void category_find_by_thirdCategory() throws Exception {
        //given
        FirstProductCategory category1 = FirstProductCategory.createFirstProductCategory("category1");
        SecondProductCategory category2 = SecondProductCategory.createSecondCategory("category2", category1);
        ThirdProductCategory category3 = ThirdProductCategory.createThirdCategory("category3", category2);

        FirstProductCategory saveFirstCate = firstProductCategoryRepository.save(category1);
        SecondProductCategory saveSecondCate = secondProductCategoryRepository.save(category2);
        ThirdProductCategory saveThirdCate = thirdProductCategoryRepository.save(category3);

        Member member = MemberAcceptanceHelper.회원가입("email", "password", memberRepository, bCryptPasswordEncoder);
        Store store = StoreAcceptanceHelper.상점생성(member, storeRepository);
        Product product1 = ProductHelper.상품생성(store, saveFirstCate, saveSecondCate, saveThirdCate, productRepository);

        //when && then
        mockMvc.perform(RestDocumentationRequestBuilders.get(CategoryViewControllerPath.CATEGORY_FIND_BY_THIRD, saveSecondCate.getNum())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(status().isOk())
                .andDo(print())
                .andDo(document("category-find-by-thirdCategory",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        pathParameters(
                                parameterWithName("thirdCategoryNum").description("third 카테고리 식별자 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("응답의 데이터 필드."),
                                subsectionWithPath("result.productSimpleResponses").description("third 카테고리에 해당하는 상품 리스트 데이터 필드."),
                                subsectionWithPath("result.pageDto").description("페이지네이션 관련 데이터 필드.")
                        )
                ));
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}
