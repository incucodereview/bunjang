package com.min.bunjang.product.service;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.category.repository.SecondProductCategoryRepository;
import com.min.bunjang.category.repository.ThirdProductCategoryRepository;
import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.common.exception.WrongRequesterException;
import com.min.bunjang.config.ServiceBaseTest;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.ProductHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.dto.request.ProductCreateOrUpdateRequest;
import com.min.bunjang.product.dto.request.ProductDeleteRequest;
import com.min.bunjang.product.dto.request.ProductTradeStateUpdateRequest;
import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductQualityState;
import com.min.bunjang.product.model.ProductTag;
import com.min.bunjang.product.model.ProductTradeState;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.product.repository.ProductTagRepository;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.model.Store;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


class ProductServiceTest extends ServiceBaseTest {
    @Autowired
    private ProductService productService;

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

    @DisplayName("상품이 생성된다.")
    @Test
    public void 상품_생성() {
        //given
        String email = "email@email.com";
        Member member = MemberHelper.회원가입(email, "password", memberRepository, bCryptPasswordEncoder);
        Store savedStore = storeRepository.save(Store.createStore("storeName", "introduce", null, member));
        FirstProductCategory firstCate = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCate = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCate));
        ThirdProductCategory thirdCate = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCate));

        ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                savedStore.getNum(),
                null,
                "productName",
                firstCate.getNum(),
                secondCate.getNum(),
                thirdCate.getNum(),
                "seoul",
                ProductQualityState.NEW_PRODUCT,
                ExchangeState.IMPOSSIBILITY,
                100000,
                DeliveryChargeInPrice.EXCLUDED,
                "제품 설명 입니다.",
                Arrays.asList("tag1", "tag2"),
                1
        );

        MemberAccount memberAccount = new MemberAccount(member);

        //when
        productService.createProduct(memberAccount, productCreateOrUpdateRequest);

        //then
        Product product = productRepository.findAll().get(0);
        Assertions.assertThat(product.getNum()).isNotNull();
        Assertions.assertThat(product.checkAndReturnStore()).isNotNull();
        Assertions.assertThat(product.getFirstProductCategory()).isNotNull();
        Assertions.assertThat(product.getSecondProductCategory()).isNotNull();
        Assertions.assertThat(product.getThirdProductCategory()).isNotNull();
        Assertions.assertThat(product.getTradeLocation()).isEqualTo(productCreateOrUpdateRequest.getTradeLocation());
        Assertions.assertThat(product.getProductQualityState()).isEqualTo(productCreateOrUpdateRequest.getProductQualityState());
        Assertions.assertThat(product.getExchangeState()).isEqualTo(productCreateOrUpdateRequest.getExchangeState());
        Assertions.assertThat(product.getProductPrice()).isEqualTo(productCreateOrUpdateRequest.getProductPrice());
        Assertions.assertThat(product.getDeliveryChargeInPrice()).isEqualTo(productCreateOrUpdateRequest.getDeliveryChargeInPrice());
        Assertions.assertThat(product.getProductExplanation()).isEqualTo(productCreateOrUpdateRequest.getProductExplanation());
        Assertions.assertThat(product.getProductAmount()).isEqualTo(productCreateOrUpdateRequest.getProductAmount());
        List<ProductTag> productTags = productTagRepository.findByProductNum(product.getNum());
        Assertions.assertThat(productTags).hasSize(2);
        Assertions.assertThat(productTags.get(0).getTag()).isEqualTo(productCreateOrUpdateRequest.getTags().get(0));
        Assertions.assertThat(productTags.get(1).getTag()).isEqualTo(productCreateOrUpdateRequest.getTags().get(1));
    }

    @DisplayName("[예외] 상품이 생성시 요청자와 상점정보가 일치하지 않을때 예외가 발생한다.")
    @Test
    public void 예외_상품생성_상점과요청자정보_불일치() {
        //given
        String email = "email@email.com";
        Member member = MemberHelper.회원가입(email, "password", memberRepository, bCryptPasswordEncoder);
        Store savedStore = storeRepository.save(Store.createStore("storeName", "introduce", null, member));
        FirstProductCategory firstCate = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCate = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCate));
        ThirdProductCategory thirdCate = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCate));

        ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                savedStore.getNum(),
                null,
                "productName",
                firstCate.getNum(),
                secondCate.getNum(),
                thirdCate.getNum(),
                "seoul",
                ProductQualityState.NEW_PRODUCT,
                ExchangeState.IMPOSSIBILITY,
                100000,
                DeliveryChargeInPrice.EXCLUDED,
                "제품 설명 입니다.",
                Arrays.asList("tag1", "tag2"),
                1
        );

        MemberAccount memberAccount = new MemberAccount(member);

        //when & then
        Assertions.assertThatThrownBy(() -> productService.createProduct(null, productCreateOrUpdateRequest)).isInstanceOf(WrongRequesterException.class);
    }

    @DisplayName("상품이 수정된다.")
    @Test
    public void 상품_변경() {
        //given
        String email = "email@email.com";
        Member member = MemberHelper.회원가입(email, "password", memberRepository, bCryptPasswordEncoder);
        Store savedStore = storeRepository.save(Store.createStore("storeName", "introduce", null, member));
        FirstProductCategory firstCate = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCate = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCate));
        ThirdProductCategory thirdCate = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCate));

        ProductCreateOrUpdateRequest productCreateRequest = new ProductCreateOrUpdateRequest(
                savedStore.getNum(),
                null,
                "productName",
                firstCate.getNum(),
                secondCate.getNum(),
                thirdCate.getNum(),
                "seoul",
                ProductQualityState.NEW_PRODUCT,
                ExchangeState.IMPOSSIBILITY,
                100000,
                DeliveryChargeInPrice.EXCLUDED,
                "제품 설명 입니다.",
                null,
                1
        );

        Product savedProduct = productRepository.save(Product.createProduct(productCreateRequest, firstCate, secondCate, thirdCate, savedStore));

        productTagRepository.saveAll(Arrays.asList(ProductTag.createProductTag("tag1", savedProduct), ProductTag.createProductTag("tag2", savedProduct)));

        ProductCreateOrUpdateRequest productUpdateRequest = new ProductCreateOrUpdateRequest(
                savedStore.getNum(),
                null,
                "updateProductName",
                firstCate.getNum(),
                secondCate.getNum(),
                thirdCate.getNum(),
                "new seoul",
                ProductQualityState.USED_PRODUCT,
                ExchangeState.POSSIBILITY,
                100000,
                DeliveryChargeInPrice.INCLUDED,
                "새로운 제품 설명 입니다.",
                Arrays.asList("tag3", "tag4"),
                1
        );

        MemberAccount memberAccount = new MemberAccount(member);

        //when
        productService.updateProduct(memberAccount, savedProduct.getNum(), productUpdateRequest);

        //then
        Product updatedProduct = productRepository.findAll().get(0);
        Assertions.assertThat(updatedProduct.getNum()).isEqualTo(savedProduct.getNum());
        Assertions.assertThat(updatedProduct.getTradeLocation()).isEqualTo(productUpdateRequest.getTradeLocation());
        Assertions.assertThat(updatedProduct.getProductQualityState()).isEqualTo(productUpdateRequest.getProductQualityState());
        Assertions.assertThat(updatedProduct.getExchangeState()).isEqualTo(productUpdateRequest.getExchangeState());
        Assertions.assertThat(updatedProduct.getProductPrice()).isEqualTo(productUpdateRequest.getProductPrice());
        Assertions.assertThat(updatedProduct.getDeliveryChargeInPrice()).isEqualTo(productUpdateRequest.getDeliveryChargeInPrice());
        Assertions.assertThat(updatedProduct.getProductExplanation()).isEqualTo(productUpdateRequest.getProductExplanation());
        Assertions.assertThat(updatedProduct.getProductAmount()).isEqualTo(productUpdateRequest.getProductAmount());
        List<ProductTag> productTags = productTagRepository.findByProductNum(updatedProduct.getNum());
        Assertions.assertThat(productTags).hasSize(2);
        Assertions.assertThat(productTags.get(0).getTag()).isEqualTo(productUpdateRequest.getTags().get(0));
        Assertions.assertThat(productTags.get(1).getTag()).isEqualTo(productUpdateRequest.getTags().get(1));
    }

    @DisplayName("상품의 거래상태가 변경된다.")
    @Test
    public void 상품_거래상태_변경() {
        //given
        String email = "email@email.com";
        Member member = MemberHelper.회원가입(email, "password", memberRepository, bCryptPasswordEncoder);
        Store savedStore = storeRepository.save(Store.createStore("storeName", "introduce", null, member));

        FirstProductCategory firstCate = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCate = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCate));
        ThirdProductCategory thirdCate = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCate));

        Product product = ProductHelper.상품생성(savedStore, firstCate, secondCate, thirdCate, productRepository);
        ProductTradeStateUpdateRequest productTradeStateUpdateRequest = new ProductTradeStateUpdateRequest(ProductTradeState.RESERVE_ING);

        MemberAccount memberAccount = new MemberAccount(member);

        //when
        productService.updateProductTradeState(memberAccount, product.getNum(), productTradeStateUpdateRequest);

        //then
        Product updatedProduct = productRepository.findById(product.getNum()).get();
        Assertions.assertThat(updatedProduct.getProductTradeState()).isEqualTo(productTradeStateUpdateRequest.getProductTradeState());
    }

    @DisplayName("상품이 삭제된다.")
    @Test
    public void 상품_삭제() {
        //given
        String email = "email@email.com";
        Member member = MemberHelper.회원가입(email, "password", memberRepository, bCryptPasswordEncoder);
        Store savedStore = storeRepository.save(Store.createStore("storeName", "introduce", null, member));

        Product product = productRepository.save(new Product("productName"));
        ProductDeleteRequest productDeleteRequest = new ProductDeleteRequest(product.getNum(), savedStore.getNum());

        MemberAccount memberAccount = new MemberAccount(member);

        //when
        productService.deleteProduct(memberAccount, productDeleteRequest);

        //then
        Optional<Product> productByNum = productRepository.findById(product.getNum());
        Assertions.assertThat(productByNum).isEmpty();
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}