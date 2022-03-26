package com.min.bunjang.integrate;

import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.integrate.config.IntegrateTestConfig;
import com.min.bunjang.login.jwt.TokenProvider;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.storereview.controller.StoreReviewControllerPath;
import com.min.bunjang.storereview.controller.StoreReviewViewControllerPath;
import com.min.bunjang.storereview.dto.request.StoreReviewCreateRequest;
import com.min.bunjang.storereview.dto.request.StoreReviewUpdateRequest;
import com.min.bunjang.storereview.model.StoreReview;
import com.min.bunjang.storereview.repository.StoreReviewRepository;
import com.min.bunjang.token.dto.TokenValuesDto;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StoreReviewIntegrateTest extends IntegrateTestConfig {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상점후기 생성 통합테스트")
    @Test
    public void storeReview_create() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberAcceptanceHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "visitor@naver.com";
        String writerPassword = "password!visitor";
        Member writerMember = MemberAcceptanceHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(writerEmail, writerPassword).getResult();

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);
        //TODO 임시 생성자로 생성해놓음.
        Product product = productRepository.save(new Product("productName"));

        double dealScore = 4.5;
        String reviewContent = "reviewContent";

        StoreReviewCreateRequest storeReviewCreateRequest = new StoreReviewCreateRequest(
                owner.getNum(),
                writer.getNum(),
                dealScore,
                product.getNum(),
                reviewContent
        );

        //! 500
        //when & then
        mockMvc.perform(post(StoreReviewControllerPath.REVIEW_CREATE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(storeReviewCreateRequest))
                        .header(TokenProvider.ACCESS_TOKEN_KEY_OF_HEADER, loginResult.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("storeReview-create",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("ownerNum").description("후기 받은 상점 식별자 정보 필드."),
                                fieldWithPath("writerNum").description("후기 작성자 식별자 정보 필드."),
                                fieldWithPath("dealScore").description("후기평점 정보 필드"),
                                fieldWithPath("productNum").description("거래한 제품의 식별자 정보 필드"),
                                fieldWithPath("reviewContent").description("후기 내용 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result.writerNum").description("후기 쓴 상점 식별자 정보 필드."),
                                fieldWithPath("result.writerThumbnail").description("후기 쓴 상점 섬네일 정보 필드."),
                                fieldWithPath("result.writerName").description("후기 쓴 상점명 정보 필드."),
                                fieldWithPath("result.dealScore").description("후기평점 정보 필드"),
                                fieldWithPath("result.productNum").description("거래한 제품의 식별자 정보 필드"),
                                fieldWithPath("result.productName").description("거래한 제품의 이름 정보 필드"),
                                fieldWithPath("result.reviewContent").description("후기 내용 정보 필드"),
                                fieldWithPath("result.postingDate").description("후기 내용 최신 변경일 정보 필드")
                        )
                ));
    }

    @DisplayName("상점후기 조회 통합테스트")
    @Test
    public void storeReview_findByOwner() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberAcceptanceHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "visitor@naver.com";
        String writerPassword = "password!visitor";
        Member writerMember = MemberAcceptanceHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(writerEmail, writerPassword).getResult();

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);
        //TODO 임시 생성자로 생성해놓음.
        Product product = productRepository.save(new Product("productName"));

        StoreReview storeReview = storeReviewRepository.save(
                StoreReview.createStoreReview(owner, writer, writer.getStoreName(), 5.0, null, product.getNum(), product.getProductName(), "reviewContent")
        );

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get(StoreReviewViewControllerPath.REVIEW_FIND_BY_STORE, owner.getNum())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_OF_HEADER, loginResult.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("storeReview-findByOwner",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        pathParameters(
                                parameterWithName("storeNum").description("후기가 달리 상점의 식별자 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        )
                ));
    }

    @Autowired
    private StoreReviewRepository storeReviewRepository;

    @DisplayName("상점후기 변경 통합테스트")
    @Test
    public void storeReview_update() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberAcceptanceHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "visitor@naver.com";
        String writerPassword = "password!visitor";
        Member writerMember = MemberAcceptanceHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(writerEmail, writerPassword).getResult();

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);
        //TODO 임시 생성자로 생성해놓음.
        Product product = productRepository.save(new Product("productName"));

        StoreReview storeReview = storeReviewRepository.save(
                StoreReview.createStoreReview(owner, writer, writer.getStoreName(), 5.0, null, product.getNum(), product.getProductName(), "reviewContent")
        );

        double updateDealScore = 4.5;
        String updatedReviewContent = "updatedReviewContent";

        StoreReviewUpdateRequest storeReviewUpdateRequest = new StoreReviewUpdateRequest(storeReview.getNum(), updateDealScore, updatedReviewContent);

        //when & then
        mockMvc.perform(put(StoreReviewControllerPath.REVIEW_UPDATE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(storeReviewUpdateRequest))
                        .header(TokenProvider.ACCESS_TOKEN_KEY_OF_HEADER, loginResult.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("storeReview-update",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("reviewNum").description("변경될 후기 식별자 정보 필드"),
                                fieldWithPath("updatedDealScore").description("변경될 거래점수 정보 필드"),
                                fieldWithPath("updatedReviewContent").description("변경될 후기내용 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("응답 데이터 정보 필드")
                        )
                ));
    }

    @DisplayName("상점후기 삭제 통합테스트")
    @Test
    public void storeReview_delete() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberAcceptanceHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "visitor@naver.com";
        String writerPassword = "password!visitor";
        Member writerMember = MemberAcceptanceHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(writerEmail, writerPassword).getResult();

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);
        //TODO 임시 생성자로 생성해놓음.
        Product product = productRepository.save(new Product("productName"));

        StoreReview storeReview = storeReviewRepository.save(
                StoreReview.createStoreReview(owner, writer, writer.getStoreName(), 5.0, null, product.getNum(), product.getProductName(), "reviewContent")
        );

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(StoreReviewControllerPath.REVIEW_DELETE, storeReview.getNum())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_OF_HEADER, loginResult.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("storeReview-delete",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        pathParameters(
                                parameterWithName("reviewNum").description("삭제될 후기 식별자 정보 필드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("요청의 성공 여부입니다. 201이면 성공, 500번 대는 실패."),
                                fieldWithPath("message").description("예외 발생시 메세지 정보 필드."),
                                fieldWithPath("result").description("응답 데이터 정보 필드")
                        )
                ));
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}
