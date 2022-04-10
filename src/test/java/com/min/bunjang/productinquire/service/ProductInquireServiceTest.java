package com.min.bunjang.productinquire.service;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.category.repository.SecondProductCategoryRepository;
import com.min.bunjang.category.repository.ThirdProductCategoryRepository;
import com.min.bunjang.config.ServiceTestConfig;
import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.dto.request.ProductCreateOrUpdateRequest;
import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductQualityState;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.productinquire.dto.request.ProductInquireCreateRequest;
import com.min.bunjang.productinquire.model.ProductInquire;
import com.min.bunjang.productinquire.repository.ProductInquireRepository;
import com.min.bunjang.store.model.Store;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

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

    @DisplayName("상품 문의를 생성하고, 태그 정보가 있다면 태그정보와 함께 생성한다. 또한 문의가 달리는 상품에 문의정보가 적용된다.")
    @Test
    void productInquire_create() {
        //given
        String ownerEmail = "urisegea@naver.com";
        String ownerPassword = "password";
        Member ownerMember = MemberAcceptanceHelper.회원가입(ownerEmail, ownerPassword, memberRepository, bCryptPasswordEncoder);

        String writerEmail = "writer@naver.com";
        String writerPassword = "password!writer";
        Member writerMember = MemberAcceptanceHelper.회원가입(writerEmail, writerPassword, memberRepository, bCryptPasswordEncoder);

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

        ProductInquireCreateRequest productInquireCreateRequest = new ProductInquireCreateRequest(writer.getNum(), savedProduct.getNum(), "문의 내용", null);
        //when
        productInquireService.createProductInquire(writerEmail, productInquireCreateRequest);


        ProductInquireCreateRequest answerProductInquireCreateRequest = new ProductInquireCreateRequest(owner.getNum(), savedProduct.getNum(), "문의 내용 답변입니다", writer.getNum());
        productInquireService.createProductInquire(ownerEmail, answerProductInquireCreateRequest);
        //then
        List<ProductInquire> all = productInquireRepository.findAll();

        Assertions.assertThat(all.get(0).getProductNum()).isEqualTo(productInquireCreateRequest.getProductNum());
        Assertions.assertThat(all.get(0).getWriterNum()).isEqualTo(productInquireCreateRequest.getWriterNum());
        Assertions.assertThat(all.get(0).getInquireContent()).isEqualTo(productInquireCreateRequest.getInquireContent());

        Assertions.assertThat(all.get(1).getProductNum()).isEqualTo(answerProductInquireCreateRequest.getProductNum());
        Assertions.assertThat(all.get(1).getWriterNum()).isEqualTo(answerProductInquireCreateRequest.getWriterNum());
        Assertions.assertThat(all.get(1).getInquireContent()).isEqualTo(answerProductInquireCreateRequest.getInquireContent());
        Assertions.assertThat(all.get(1).getMentionedStoreNumForAnswer()).isEqualTo(answerProductInquireCreateRequest.getMentionedStoreNumForAnswer());


        Product product = productRepository.findById(savedProduct.getNum()).get();
        Assertions.assertThat(product.getProductInquires()).isNotNull();
    }
}