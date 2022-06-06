package com.min.bunjang.integrate;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.config.IntegrateBaseTest;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.StoreHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.controller.ProductControllerPath;
import com.min.bunjang.product.controller.ProductViewControllerPath;
import com.min.bunjang.product.dto.request.ProductCreateOrUpdateRequest;
import com.min.bunjang.product.dto.request.ProductTradeStateUpdateRequest;
import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductQualityState;
import com.min.bunjang.product.model.ProductTradeState;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.token.jwt.TokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductIntegrateTest extends IntegrateBaseTest {

    @DisplayName("상품생성 통합테스트")
    @Test
    public void 상품생성() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto tokenValuesDto = MemberHelper.로그인(email, password, loginService);

        Store store = StoreHelper.상점생성(member, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                store.getNum(),
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

        //when
        mockMvc.perform(post(ProductControllerPath.PRODUCT_CREATE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, tokenValuesDto.getAccessToken())
                        .content(objectMapper.writeValueAsString(productCreateOrUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        List<Product> products = productRepository.findAll();
        Assertions.assertThat(products).hasSize(1);
    }

    @DisplayName("상품 단건 조회 통합테스트")
    @Test
    public void 상품_단건조회() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto tokenValuesDto = MemberHelper.로그인(email, password, loginService);

        Store store = StoreHelper.상점생성(member, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                store.getNum(),
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

        Product product = productRepository.save(Product.createProduct(productCreateOrUpdateRequest, firstCategory, secondCategory, thirdCategory, store));

        //when & then
        String path = ProductViewControllerPath.PRODUCT_GET.replace("{productNum}", String.valueOf(product.getNum()));
        mockMvc.perform(get(path)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("result.productNum").value(product.getNum()))
                .andDo(print());
    }

    @DisplayName("상점별 상품 목록 조회 통합테스트")
    @Test
    public void 상점별_상품목록_조회() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto tokenValuesDto = MemberHelper.로그인(email, password, loginService);

        Store store = StoreHelper.상점생성(member, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                store.getNum(),
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

        Product product = productRepository.save(Product.createProduct(productCreateOrUpdateRequest, firstCategory, secondCategory, thirdCategory, store));

        //when & then
        String path = ProductViewControllerPath.PRODUCTS_FIND_BY_STORE.replace("{storeNum}", String.valueOf(store.getNum()));
        mockMvc.perform(get(path)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").isNotEmpty())
                .andDo(print());
    }

    @DisplayName("상품 거래상태 변경 통합테스트")
    @Test
    public void 상품_거래상태_변경() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto tokenValuesDto = MemberHelper.로그인(email, password, loginService);

        Store store = StoreHelper.상점생성(member, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                store.getNum(),
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

        Product product = productRepository.save(Product.createProduct(productCreateOrUpdateRequest, firstCategory, secondCategory, thirdCategory, store));

        ProductTradeStateUpdateRequest productTradeStateUpdateRequest = new ProductTradeStateUpdateRequest(ProductTradeState.SOLD_OUT);

        //when
        String path = ProductControllerPath.PRODUCT_UPDATE_TRADE_STATE.replace("{productNum}", String.valueOf(product.getNum()));
        mockMvc.perform(patch(path)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, tokenValuesDto.getAccessToken())
                        .content(objectMapper.writeValueAsString(productTradeStateUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        Product updatedProduct = productRepository.findById(product.getNum()).get();
        Assertions.assertThat(updatedProduct.getProductTradeState()).isEqualTo(productTradeStateUpdateRequest.getProductTradeState());
    }

    @DisplayName("상품 수정 통합테스트")
    @Test
    public void 상품_수정() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto tokenValuesDto = MemberHelper.로그인(email, password, loginService);

        Store store = StoreHelper.상점생성(member, storeRepository);

        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        ProductCreateOrUpdateRequest productCreateRequest = new ProductCreateOrUpdateRequest(
                store.getNum(),
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

        Product product = productRepository.save(Product.createProduct(productCreateRequest, firstCategory, secondCategory, thirdCategory, store));

        ProductCreateOrUpdateRequest productUpdateRequest = new ProductCreateOrUpdateRequest(
                store.getNum(),
                null,
                "updateProductName",
                firstCategory.getNum(),
                secondCategory.getNum(),
                thirdCategory.getNum(),
                "seoul gangnam",
                ProductQualityState.NEW_PRODUCT,
                ExchangeState.POSSIBILITY,
                100000,
                DeliveryChargeInPrice.EXCLUDED,
                "업데이트 된 제품 설명 입니다.",
                Arrays.asList("tag2", "tag3"),
                1
        );

        //when
        String path = ProductControllerPath.PRODUCT_UPDATE.replace("{productNum}", String.valueOf(product.getNum()));
        mockMvc.perform(put(path)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, tokenValuesDto.getAccessToken())
                        .content(objectMapper.writeValueAsString(productUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        Product updatedProduct = productRepository.findById(product.getNum()).get();
        Assertions.assertThat(updatedProduct.getProductName()).isEqualTo(productUpdateRequest.getProductName());
        Assertions.assertThat(updatedProduct.getTradeLocation()).isEqualTo(productUpdateRequest.getTradeLocation());
        Assertions.assertThat(updatedProduct.getExchangeState()).isEqualTo(productUpdateRequest.getExchangeState());
        Assertions.assertThat(updatedProduct.getProductExplanation()).isEqualTo(productUpdateRequest.getProductExplanation());
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
