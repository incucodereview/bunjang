package com.min.bunjang.integrate;

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
import com.min.bunjang.storereview.controller.StoreReviewControllerPath;
import com.min.bunjang.storereview.controller.StoreReviewViewControllerPath;
import com.min.bunjang.storereview.dto.request.StoreReviewCreateRequest;
import com.min.bunjang.storereview.dto.request.StoreReviewUpdateRequest;
import com.min.bunjang.storereview.model.StoreReview;
import com.min.bunjang.storereview.repository.StoreReviewRepository;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class StoreReviewIntegrateTest extends IntegrateBaseTest {

    @Autowired
    private StoreReviewRepository storeReviewRepository;

    @DisplayName("상점후기 생성 통합테스트")
    @Test
    public void 상점후기_생성() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(writerEmail, writerPassword, loginService);

        Store owner = StoreHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreHelper.상점생성(writerMember, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));
        Product product = ProductHelper.상품생성(owner, firstCategory, secondCategory, thirdCategory, productRepository);

        StoreReviewCreateRequest storeReviewCreateRequest = new StoreReviewCreateRequest(owner.getNum(), writer.getNum(), 3.5, product.getNum(), "후기 내용!");

        //when
        postRequest(StoreReviewControllerPath.REVIEW_CREATE, loginResult.getAccessToken(), storeReviewCreateRequest);

        //then
        List<StoreReview> storeReviews = storeReviewRepository.findAll();
        Assertions.assertThat(storeReviews).hasSize(1);
    }

    @DisplayName("특정 상점의 상점후기 목록 조회 통합테스트")
    @Test
    public void 상점_상점후기_목록_조회() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(writerEmail, writerPassword, loginService);

        Store owner = StoreHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreHelper.상점생성(writerMember, storeRepository);
        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));
        Product product = ProductHelper.상품생성(owner, firstCategory, secondCategory, thirdCategory, productRepository);

        StoreReview ownerStoreReview = storeReviewRepository.save(StoreReview.createStoreReview(owner, writer, writer.getStoreName(), 3.5, product.getNum(), product.getProductName(), "상점 후기"));
        StoreReview writerStoreReview = storeReviewRepository.save(StoreReview.createStoreReview(writer, owner, owner.getStoreName(), 3, product.getNum(), product.getProductName(), "작성자 상점 후기"));

        //when & then
        String path = StoreReviewViewControllerPath.REVIEW_FIND_BY_STORE.replace("{storeNum}", String.valueOf(owner.getNum()));
        ResultActions resultActions = getRequest(loginResult.getAccessToken(), path);
        resultActions.andExpect(jsonPath("result.storeReviewListResponses.[0]").isNotEmpty());
        resultActions.andExpect(jsonPath("result.storeReviewListResponses.[1]").doesNotExist());
    }

    @DisplayName("상점후기 변경 통합테스트")
    @Test
    public void 상점후기_변경() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(writerEmail, writerPassword, loginService);

        Store owner = StoreHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreHelper.상점생성(writerMember, storeRepository);
        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));
        Product product = ProductHelper.상품생성(owner, firstCategory, secondCategory, thirdCategory, productRepository);

        StoreReview storeReview = storeReviewRepository.save(StoreReview.createStoreReview(owner, writer, writer.getStoreName(), 3.5, product.getNum(), product.getProductName(), "상점 후기"));

        StoreReviewUpdateRequest storeReviewUpdateRequest = new StoreReviewUpdateRequest(storeReview.getNum(), 4.0, "업데이트 리뷰 내용");
        //when
        putRequest(loginResult, storeReviewUpdateRequest);

        //then
        StoreReview updatedStoreReview = storeReviewRepository.findById(storeReview.getNum()).get();
        Assertions.assertThat(updatedStoreReview.getDealScore()).isEqualTo(storeReviewUpdateRequest.getUpdatedDealScore());
    }

    @DisplayName("상점후기 삭제 통합테스트")
    @Test
    public void 상점후기_삭제() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(writerEmail, writerPassword, loginService);

        Store owner = StoreHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreHelper.상점생성(writerMember, storeRepository);
        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));
        Product product = ProductHelper.상품생성(owner, firstCategory, secondCategory, thirdCategory, productRepository);

        StoreReview storeReview = storeReviewRepository.save(StoreReview.createStoreReview(owner, writer, writer.getStoreName(), 3.5, product.getNum(), product.getProductName(), "상점 후기"));

        //when
        String path = StoreReviewControllerPath.REVIEW_DELETE.replace("{reviewNum}", String.valueOf(storeReview.getNum()));
        deleteRequest(path, loginResult.getAccessToken(), null);

        //then
        List<StoreReview> storeReviews = storeReviewRepository.findAll();
        Assertions.assertThat(storeReviews).hasSize(0);
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
