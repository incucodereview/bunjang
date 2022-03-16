package com.min.bunjang.productinquire.service;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.category.repository.SecondProductCategoryRepository;
import com.min.bunjang.category.repository.ThirdProductCategoryRepository;
import com.min.bunjang.config.ServiceTestConfig;
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
import com.min.bunjang.productinquire.dto.ProductInquireCreateRequest;
import com.min.bunjang.productinquire.model.ProductInquire;
import com.min.bunjang.productinquire.repository.ProductInquireRepository;
import com.min.bunjang.store.model.Store;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

class ProductInquireServiceTest extends ServiceTestConfig {
    @Autowired
    private ProductInquireService productInquireService;

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

    @DisplayName("상품 문의를 생성하고, 상품에 문의정보가 적용된다.")
    @Test
    void productInquire_create() {
        //given
        Member member = Member.createMember(MemberDirectCreateDto.of("email", "pwd", "name", null, null, MemberRole.ROLE_MEMBER));
        Member savedMember = memberRepository.save(member);
        Store savedStore = storeRepository.save(Store.createStore("storeName", "introduce", null, member));
        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));
        ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                savedStore.getNum(),
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
        Product savedProduct = productRepository.save(Product.createProduct(productCreateOrUpdateRequest, firstCategory, secondCategory, thirdCategory, savedStore));

        ProductInquireCreateRequest productInquireCreateRequest = new ProductInquireCreateRequest(savedStore.getNum(), savedProduct.getNum(), "문의 내용", null);
        //when
        productInquireService.createProductInquire(savedMember.getEmail(), productInquireCreateRequest);

        //then
        ProductInquire productInquire = productInquireRepository.findAll().get(0);

        Assertions.assertThat(productInquire.getProductNum()).isEqualTo(productInquireCreateRequest.getProductNum());
        Assertions.assertThat(productInquire.getWriterNum()).isEqualTo(productInquireCreateRequest.getWriterNum());
        Assertions.assertThat(productInquire.getInquireContent()).isEqualTo(productInquireCreateRequest.getInquireContent());

        Product product = productRepository.findById(savedProduct.getNum()).get();
        Assertions.assertThat(product.getProductInquires()).isNotNull();
    }
}