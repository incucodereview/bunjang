package com.min.bunjang.acceptance.product;

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
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.controller.ProductControllerPath;
import com.min.bunjang.product.dto.ProductCreateOrUpdateRequest;
import com.min.bunjang.product.dto.ProductDeleteRequest;
import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductState;
import com.min.bunjang.product.model.ProductTag;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.product.repository.ProductTagRepository;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ProductAcceptanceTest extends AcceptanceTestConfig {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTagRepository productTagRepository;

    @Autowired
    private FirstProductCategoryRepository firstProductCategoryRepository;

    @Autowired
    private SecondProductCategoryRepository secondProductCategoryRepository;

    @Autowired
    private ThirdProductCategoryRepository thirdProductCategoryRepository;

    @TestFactory
    Stream<DynamicTest> dynamicTestStream() {
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberAcceptanceHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(email, password).getResult();
        Store store = StoreAcceptanceHelper.상점생성(member, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        return Stream.of(
                DynamicTest.dynamicTest("상품 생성.", () -> {
                    //given
                    ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                            store.getNum(),
                            null,
                            "productName",
                            firstCategory.getNum(),
                            secondCategory.getNum(),
                            thirdCategory.getNum(),
                            "seoul",
                            ProductState.NEW_PRODUCT,
                            ExchangeState.IMPOSSIBILITY,
                            100000,
                            DeliveryChargeInPrice.EXCLUDED,
                            "제품 설명 입니다.",
                            Arrays.asList("tag1", "tag2"),
                            1
                    );

                    //when
                    상품_생성_요청(loginResult, productCreateOrUpdateRequest);

                    //then
                    상품_생성_응답_검증(productCreateOrUpdateRequest);
                }),

                DynamicTest.dynamicTest("상품 수정.", () -> {
                    //given
                    Product product = productRepository.findAll().get(0);
                    ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                            store.getNum(),
                            null,
                            "updatedProductName",
                            firstCategory.getNum(),
                            secondCategory.getNum(),
                            thirdCategory.getNum(),
                            "new seoul",
                            ProductState.USED_PRODUCT,
                            ExchangeState.POSSIBILITY,
                            100214,
                            DeliveryChargeInPrice.INCLUDED,
                            "새로운 제품 설명 입니다.",
                            Arrays.asList("tag3", "tag2"),
                            2
                    );

                    //when
                    상품_수정_요청(loginResult, product, productCreateOrUpdateRequest);

                    //then
                    상품_수정_응답_검증(product, productCreateOrUpdateRequest);
                }),

                DynamicTest.dynamicTest("상품 삭제.", () -> {
                    //given
                    Product product = productRepository.findAll().get(0);

                    ProductDeleteRequest productDeleteRequest = new ProductDeleteRequest(product.getNum(), store.getNum());
                    //when
                    상품_삭제_요청(loginResult, productDeleteRequest);

                    //then
                    상품_삭제_응답_검증();
                })
        );

    }

    private RestResponse<Void> 상품_생성_요청(TokenValuesDto loginResult, ProductCreateOrUpdateRequest productCreateOrUpdateRequest) {
        return postApi(ProductControllerPath.PRODUCT_CREATE, productCreateOrUpdateRequest, new TypeReference<RestResponse<Void>>() {
        }, loginResult.getAccessToken());
    }

    private void 상품_생성_응답_검증(ProductCreateOrUpdateRequest productCreateOrUpdateRequest) {
        Product product = productRepository.findAll().get(0);
        Assertions.assertThat(product.getNum()).isNotNull();
        Assertions.assertThat(product.getFirstProductCategory()).isNotNull();
        Assertions.assertThat(product.getSecondProductCategory()).isNotNull();
        Assertions.assertThat(product.getThirdProductCategory()).isNotNull();
        Assertions.assertThat(product.getProductName()).isEqualTo(productCreateOrUpdateRequest.getProductName());
        Assertions.assertThat(product.getExchangeLocation()).isEqualTo(productCreateOrUpdateRequest.getExchangeLocation());
        Assertions.assertThat(product.getProductState()).isEqualTo(productCreateOrUpdateRequest.getProductState());
        Assertions.assertThat(product.getExchangeState()).isEqualTo(productCreateOrUpdateRequest.getExchangeState());
        Assertions.assertThat(product.getDeliveryChargeInPrice()).isEqualTo(productCreateOrUpdateRequest.getDeliveryChargeInPrice());
        Assertions.assertThat(product.getProductAmount()).isEqualTo(productCreateOrUpdateRequest.getProductAmount());
        List<ProductTag> productTags = productTagRepository.findByProductNum(product.getNum());
        Assertions.assertThat(productTags).hasSize(2);
        Assertions.assertThat(productTags.get(0).getTag()).isEqualTo(productCreateOrUpdateRequest.getTags().get(0));
        Assertions.assertThat(productTags.get(1).getTag()).isEqualTo(productCreateOrUpdateRequest.getTags().get(1));
    }

    private void 상품_수정_요청(TokenValuesDto loginResult, Product product, ProductCreateOrUpdateRequest productCreateOrUpdateRequest) {
        String path = ProductControllerPath.PRODUCT_UPDATE.replace("{productNum}", String.valueOf(product.getNum()));
        putApi(path, productCreateOrUpdateRequest, new TypeReference<RestResponse<Void>>() {}, loginResult.getAccessToken());
    }

    private void 상품_수정_응답_검증(Product product, ProductCreateOrUpdateRequest productCreateOrUpdateRequest) {
        Product updatedProduct = productRepository.findAll().get(0);
        Assertions.assertThat(updatedProduct.getNum()).isEqualTo(product.getNum());
        Assertions.assertThat(updatedProduct.getProductName()).isEqualTo(productCreateOrUpdateRequest.getProductName());
        Assertions.assertThat(updatedProduct.getExchangeLocation()).isEqualTo(productCreateOrUpdateRequest.getExchangeLocation());
        Assertions.assertThat(updatedProduct.getProductState()).isEqualTo(productCreateOrUpdateRequest.getProductState());
        Assertions.assertThat(updatedProduct.getExchangeState()).isEqualTo(productCreateOrUpdateRequest.getExchangeState());
        Assertions.assertThat(updatedProduct.getDeliveryChargeInPrice()).isEqualTo(productCreateOrUpdateRequest.getDeliveryChargeInPrice());
        Assertions.assertThat(updatedProduct.getProductAmount()).isEqualTo(productCreateOrUpdateRequest.getProductAmount());
        List<ProductTag> productTags = productTagRepository.findByProductNum(product.getNum());
        Assertions.assertThat(productTags).hasSize(2);
        Assertions.assertThat(productTags.get(0).getTag()).isEqualTo(productCreateOrUpdateRequest.getTags().get(0));
        Assertions.assertThat(productTags.get(1).getTag()).isEqualTo(productCreateOrUpdateRequest.getTags().get(1));
    }

    private void 상품_삭제_요청(TokenValuesDto loginResult, ProductDeleteRequest productDeleteRequest) {
        deleteApi(ProductControllerPath.PRODUCT_DELETE, productDeleteRequest, new TypeReference<RestResponse<Void>>() {}, loginResult.getAccessToken());
    }

    private void 상품_삭제_응답_검증() {
        List<Product> products = productRepository.findAll();
        Assertions.assertThat(products).isEmpty();
    }
}