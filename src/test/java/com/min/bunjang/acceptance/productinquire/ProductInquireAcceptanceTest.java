package com.min.bunjang.acceptance.productinquire;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.acceptance.common.AcceptanceTestConfig;
import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.category.repository.SecondProductCategoryRepository;
import com.min.bunjang.category.repository.ThirdProductCategoryRepository;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.dto.request.ProductCreateOrUpdateRequest;
import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductQualityState;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.productinquire.controller.ProductInquireControllerPath;
import com.min.bunjang.productinquire.controller.ProductInquireViewControllerPath;
import com.min.bunjang.productinquire.dto.request.ProductInquireCreateRequest;
import com.min.bunjang.productinquire.dto.response.ProductInquireResponse;
import com.min.bunjang.productinquire.dto.response.ProductInquireResponses;
import com.min.bunjang.productinquire.model.ProductInquire;
import com.min.bunjang.productinquire.repository.ProductInquireRepository;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ProductInquireAcceptanceTest extends AcceptanceTestConfig {

    @Autowired
    private ProductInquireRepository productInquireRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FirstProductCategoryRepository firstProductCategoryRepository;

    @Autowired
    private SecondProductCategoryRepository secondProductCategoryRepository;

    @Autowired
    private ThirdProductCategoryRepository thirdProductCategoryRepository;

    @TestFactory
    Stream<DynamicTest> dynamicTestStream() throws JsonProcessingException {
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberHelper.인수테스트_로그인(writerEmail, writerPassword).getResult();

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);
        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));
        ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                null,
                null,
                "productName",
                firstCategory.getNum(),
                secondCategory.getNum(),
                thirdCategory.getNum(),
                "seoul",
                ProductQualityState.NEW_PRODUCT,
                ExchangeState.IMPOSSIBILITY,
                100000,
                DeliveryChargeInPrice.EXCLUDED,
                "제품 설명 입니다.",
                Arrays.asList("tag1", "tag2"),
                1
        );
        Product savedProduct = productRepository.save(Product.createProduct(productCreateOrUpdateRequest, firstCategory, secondCategory, thirdCategory, owner));

        return Stream.of(
                DynamicTest.dynamicTest("상품 문의 생성", () -> {
                    //given
                    ProductInquireCreateRequest productInquireCreateRequest = new ProductInquireCreateRequest(writer.getNum(), savedProduct.getNum(), "문의 내용 입니다.", null);

                    //when
                    상품문의_생성_요청(loginResult, productInquireCreateRequest);

                    //then
                    상품문의_생성_응답_검증(writer, savedProduct, productInquireCreateRequest);

                }),

                //! nullP
                DynamicTest.dynamicTest("상품문의 목록 조회", () -> {
                    //given
                    Long productNum = savedProduct.getNum();

                    //when
                    ProductInquireResponses productInquireResponses = 상품문의_목록_조회_요청(loginResult, productNum);

                    //then
                    상품문의_목록_조회_응답_검증(productInquireResponses);
                }),

                DynamicTest.dynamicTest("상품 문의 삭제", () -> {
                    //given
                    ProductInquire productInquire = productInquireRepository.findAll().get(0);

                    //when
                    상품문의_삭제_요청(loginResult, productInquire);

                    //then
                    상품문의_삭제_응답_검증();
                })

        );
    }

    private void 상품문의_생성_요청(TokenValuesDto loginResult, ProductInquireCreateRequest productInquireCreateRequest) throws JsonProcessingException {
        postRequest(ProductInquireControllerPath.PRODUCT_INQUIRE_CREATE, productInquireCreateRequest, new TypeReference<RestResponse<Void>>() {}, loginResult.getAccessToken());
    }

    private void 상품문의_생성_응답_검증(Store writer, Product savedProduct, ProductInquireCreateRequest productInquireCreateRequest) {
        ProductInquire productInquire = productInquireRepository.findAll().get(0);
        Assertions.assertThat(productInquire.getWriterNum()).isEqualTo(writer.getNum());
        Assertions.assertThat(productInquire.getWriterName()).isEqualTo(writer.getStoreName());
        Assertions.assertThat(productInquire.getProductNum()).isEqualTo(savedProduct.getNum());
        Assertions.assertThat(productInquire.getInquireContent()).isEqualTo(productInquireCreateRequest.getInquireContent());
        Assertions.assertThat(productInquire.getMentionedStoreNumForAnswer()).isEqualTo(productInquireCreateRequest.getMentionedStoreNumForAnswer());
    }

    private ProductInquireResponses 상품문의_목록_조회_요청(TokenValuesDto loginResult, Long productNum) {
        String path = ProductInquireViewControllerPath.PRODUCT_INQUIRE_FIND_BY_PRODUCT.replace("{productNum}", String.valueOf(productNum));
        ProductInquireResponses productInquireResponses = getRequest(path, loginResult.getAccessToken(), new TypeReference<RestResponse<ProductInquireResponses>>() {
        }).getResult();
        return productInquireResponses;
    }

    private void 상품문의_목록_조회_응답_검증(ProductInquireResponses productInquireResponses) {
        List<ProductInquireResponse> productInquireResponseList = productInquireResponses.getProductInquireResponses();
        Assertions.assertThat(productInquireResponseList).hasSize(1);
    }

    private void 상품문의_삭제_요청(TokenValuesDto loginResult, ProductInquire productInquire) throws JsonProcessingException {
        String path = ProductInquireControllerPath.PRODUCT_INQUIRE_DELETE.replace("{inquireNum}", String.valueOf(productInquire.getNum()));
        deleteRequest(path, null, new TypeReference<RestResponse<Void>>() {}, loginResult.getAccessToken());
    }

    private void 상품문의_삭제_응답_검증() {
        List<ProductInquire> allProductInquiries = productInquireRepository.findAll();
        Assertions.assertThat(allProductInquiries).isEmpty();
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
