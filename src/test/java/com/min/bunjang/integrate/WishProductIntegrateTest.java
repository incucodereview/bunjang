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
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.wishproduct.controller.WishProductControllerPath;
import com.min.bunjang.wishproduct.controller.WishProductViewControllerPath;
import com.min.bunjang.wishproduct.dto.request.WishProductCreateRequest;
import com.min.bunjang.wishproduct.dto.request.WishProductsDeleteRequest;
import com.min.bunjang.wishproduct.model.WishProduct;
import com.min.bunjang.wishproduct.repository.WishProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class WishProductIntegrateTest extends IntegrateBaseTest {

    @Autowired
    private WishProductRepository wishProductRepository;

    @DisplayName("찜상품 추가")
    @Test
    public void 찜상품_추가() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(writerEmail, writerPassword, loginService);

        Store owner = StoreHelper.상점생성(ownerMember, storeRepository);
        Store selector = StoreHelper.상점생성(writerMember, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        Product product = ProductHelper.상품생성(owner, firstCategory, secondCategory, thirdCategory, productRepository);

        WishProductCreateRequest wishProductCreateRequest = new WishProductCreateRequest(selector.getNum(), product.getNum());

        //when
        postRequest(WishProductControllerPath.WISH_PRODUCT_CREATE, loginResult.getAccessToken(), wishProductCreateRequest);

        //then
        List<WishProduct> wishProducts = wishProductRepository.findAll();
        Assertions.assertThat(wishProducts).hasSize(1);
    }

    @DisplayName("상점의 찜목록 조회")
    @Test
    public void 상점_찜목록_조회() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(writerEmail, writerPassword, loginService);

        Store owner = StoreHelper.상점생성(ownerMember, storeRepository);
        Store selector = StoreHelper.상점생성(writerMember, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        Product product = ProductHelper.상품생성(owner, firstCategory, secondCategory, thirdCategory, productRepository);

        WishProduct wishProduct = wishProductRepository.save(new WishProduct(selector, product));

        //when & then
        String path = WishProductViewControllerPath.WISH_PRODUCT_FIND_BY_STORE.replace("{storeNum}", String.valueOf(selector.getNum()));
        ResultActions resultActions = getRequest(loginResult.getAccessToken(), path);
        resultActions.andExpect(jsonPath("result.wishProductResponses.[0]").isNotEmpty());
        resultActions.andExpect(jsonPath("result.wishProductResponses.[1]").doesNotExist());
    }

    @DisplayName("찜상품 삭제")
    @Test
    public void 찜상품_삭제() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(writerEmail, writerPassword, loginService);

        Store owner = StoreHelper.상점생성(ownerMember, storeRepository);
        Store selector = StoreHelper.상점생성(writerMember, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        Product product = ProductHelper.상품생성(owner, firstCategory, secondCategory, thirdCategory, productRepository);

        WishProduct wishProduct = wishProductRepository.save(new WishProduct(selector, product));

        WishProductsDeleteRequest wishProductsDeleteRequest = new WishProductsDeleteRequest(Arrays.asList(wishProduct.getNum()), selector.getNum());

        //when
        deleteRequest(WishProductControllerPath.WISH_PRODUCT_DELETE, loginResult.getAccessToken(), wishProductsDeleteRequest);

        //then
        List<WishProduct> wishProducts = wishProductRepository.findAll();
        Assertions.assertThat(wishProducts).hasSize(0);
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
