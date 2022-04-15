package com.min.bunjang.integrate;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.helpers.ProductHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.integrate.config.IntegrateTestConfig;
import com.min.bunjang.token.jwt.TokenProvider;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.wishproduct.controller.WishProductControllerPath;
import com.min.bunjang.wishproduct.controller.WishProductViewControllerPath;
import com.min.bunjang.wishproduct.dto.request.WishProductCreateRequest;
import com.min.bunjang.wishproduct.dto.request.WishProductsDeleteRequest;
import com.min.bunjang.wishproduct.model.WishProduct;
import com.min.bunjang.wishproduct.repository.WishProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.Arrays;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WishProductIntegrateTest extends IntegrateTestConfig {
    @Autowired
    private WishProductRepository wishProductRepository;

    @DisplayName("찜상품 생성 통합테스트")
    @Test
    public void wishProduct_create() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberAcceptanceHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "visitor@naver.com";
        String writerPassword = "password!visitor";
        Member writerMember = MemberAcceptanceHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(ownerEmail, ownerPassword).getResult();

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        Product product = ProductHelper.상품생성(owner, firstCategory, secondCategory, thirdCategory, productRepository);

        WishProductCreateRequest wishProductCreateRequest = new WishProductCreateRequest(owner.getNum(), product.getNum());

        //when & then
        mockMvc.perform(post(WishProductControllerPath.WISH_PRODUCT_CREATE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(wishProductCreateRequest))
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, loginResult.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("wishProduct-create",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("storeNum").description("상품을 찜한 상점의 식별자 정보 필드"),
                                fieldWithPath("productNum").description("찜받은 상품의 식별자 정보 필드.")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("후기 쓴 상점 식별자 정보 필드.")
                        )
                ));
    }

    @DisplayName("찜상품 생성 통합테스트")
    @Test
    public void wishProduct_findAll_byStore() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberAcceptanceHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "visitor@naver.com";
        String writerPassword = "password!visitor";
        Member writerMember = MemberAcceptanceHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(ownerEmail, ownerPassword).getResult();

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        Product product = ProductHelper.상품생성(owner, firstCategory, secondCategory, thirdCategory, productRepository);

        WishProductCreateRequest wishProductCreateRequest = new WishProductCreateRequest(owner.getNum(), product.getNum());

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get(WishProductViewControllerPath.WISH_PRODUCT_FIND_BY_STORE, owner.getNum())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, loginResult.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("wishProduct-findAll-byStore",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        pathParameters(
                                parameterWithName("storeNum").description("상품을 찜한 상점의 식별자 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        )
                ));
    }

    @DisplayName("찜상품 삭제 통합테스트")
    @Test
    public void wishProduct_delete() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberAcceptanceHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "visitor@naver.com";
        String writerPassword = "password!visitor";
        Member writerMember = MemberAcceptanceHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(ownerEmail, ownerPassword).getResult();

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        Product product = ProductHelper.상품생성(owner, firstCategory, secondCategory, thirdCategory, productRepository);

        wishProductRepository.save(new WishProduct(owner, product));

        WishProductsDeleteRequest wishProductsDeleteRequest = new WishProductsDeleteRequest(Arrays.asList(1L), owner.getNum());

        //when & then
        mockMvc.perform(delete(WishProductControllerPath.WISH_PRODUCT_DELETE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(wishProductsDeleteRequest))
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, loginResult.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("wishProduct-delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("wishProductNumsForDelete").description("찜목록 삭제 id 목록 정보 필드"),
                                fieldWithPath("storeNum").description("상품을 찜한 상점의 식별자 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("후기 쓴 상점 식별자 정보 필드.")
                        )
                ));
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}
