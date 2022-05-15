package com.min.bunjang.integrate;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.config.IntegrateBaseTest;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.ProductHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.productinquire.controller.ProductInquireControllerPath;
import com.min.bunjang.productinquire.controller.ProductInquireViewControllerPath;
import com.min.bunjang.productinquire.dto.request.ProductInquireCreateRequest;
import com.min.bunjang.productinquire.model.ProductInquire;
import com.min.bunjang.productinquire.repository.ProductInquireRepository;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.token.jwt.TokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ProductInquireIntegrateTest extends IntegrateBaseTest {

    @Autowired
    private ProductInquireRepository productInquireRepository;

    @DisplayName("상품 문의 생성 통합테스트")
    @Test
    public void 상품문의_생성() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(writerEmail, writerPassword, loginService);

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);
        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));
        Product product = ProductHelper.상품생성(owner, firstCategory, secondCategory, thirdCategory, productRepository);

        ProductInquireCreateRequest productInquireCreateRequest = new ProductInquireCreateRequest(writer.getNum(), product.getNum(), "상품문의!", null);

        //when
        postRequest(ProductInquireControllerPath.PRODUCT_INQUIRE_CREATE, loginResult.getAccessToken(), productInquireCreateRequest);

        //then
        List<ProductInquire> productInquires = productInquireRepository.findAll();
        Assertions.assertThat(productInquires).hasSize(1);
    }

    @DisplayName("상품 문의 목록 조회 통합테스트")
    @Test
    public void 상품문의_목록_조회() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(writerEmail, writerPassword, loginService);

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);
        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        Product product = ProductHelper.상품생성(owner, firstCategory, secondCategory, thirdCategory, productRepository);
        productInquireRepository.save(ProductInquire.createProductInquire(writer.getNum(), writer.getStoreName(), product.getNum(), "inquire"));

        //when & then
        String path = ProductInquireViewControllerPath.PRODUCT_INQUIRE_FIND_BY_PRODUCT.replace("{productNum}", String.valueOf(product.getNum()));
        ResultActions resultActions = getRequest(loginResult.getAccessToken(), path);
        resultActions.andExpect(jsonPath("result.productInquireResponses").isNotEmpty());
    }

    @DisplayName("상품 문의 삭제 통합테스트")
    @Test
    public void 상품문의_삭제() throws Exception {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.로그인(writerEmail, writerPassword, loginService);

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);
        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        Product product = ProductHelper.상품생성(owner, firstCategory, secondCategory, thirdCategory, productRepository);
        ProductInquire productInquire = productInquireRepository.save(ProductInquire.createProductInquire(writer.getNum(), writer.getStoreName(), product.getNum(), "inquire"));

        //when
        String path = ProductInquireControllerPath.PRODUCT_INQUIRE_DELETE.replace("{inquireNum}", String.valueOf(productInquire.getNum()));
        deleteRequest(path, loginResult.getAccessToken(), null);

        //then
        List<ProductInquire> productInquires = productInquireRepository.findAll();
        Assertions.assertThat(productInquires).hasSize(0);
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
