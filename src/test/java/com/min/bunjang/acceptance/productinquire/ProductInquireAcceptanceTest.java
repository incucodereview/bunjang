package com.min.bunjang.acceptance.productinquire;

import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.acceptance.common.AcceptanceTestConfig;
import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.category.repository.SecondProductCategoryRepository;
import com.min.bunjang.category.repository.ThirdProductCategoryRepository;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.product.dto.ProductCreateOrUpdateRequest;
import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductQualityState;
import com.min.bunjang.product.model.ProductTradeState;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.productinquire.controller.ProductInquireControllerPath;
import com.min.bunjang.productinquire.dto.ProductInquireCreateRequest;
import com.min.bunjang.productinquire.model.ProductInquire;
import com.min.bunjang.productinquire.repository.ProductInquireRepository;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
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
    Stream<DynamicTest> dynamicTestStream() {
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberAcceptanceHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberAcceptanceHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(writerEmail, writerPassword).getResult();

        Store owner = StoreAcceptanceHelper.상점생성(ownerMember, storeRepository);
        Store writer = StoreAcceptanceHelper.상점생성(writerMember, storeRepository);
        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));
        ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                owner.getNum(),
                null,
                "productName",
                firstCategory.getNum(),
                secondCategory.getNum(),
                thirdCategory.getNum(),
                "seoul",
                ProductTradeState.SOLD_ING,
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
                    postApi(ProductInquireControllerPath.PRODUCT_INQUIRE_CREATE, productInquireCreateRequest, new TypeReference<RestResponse<Void>>() {}, loginResult.getAccessToken());

                    //then
                    ProductInquire productInquire = productInquireRepository.findAll().get(0);
                    Assertions.assertThat(productInquire.getWriterNum()).isEqualTo(writer.getNum());
                    Assertions.assertThat(productInquire.getProductNum()).isEqualTo(savedProduct.getNum());
                    Assertions.assertThat(productInquire.getInquireContent()).isEqualTo(productInquireCreateRequest.getInquireContent());
                    Assertions.assertThat(productInquire.getInquireWriterNumForAnswer()).isEqualTo(productInquireCreateRequest.getInquireWriterNumForAnswer());

                })
        );
    }
}
