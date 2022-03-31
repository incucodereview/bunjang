package com.min.bunjang.product.service;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.category.repository.SecondProductCategoryRepository;
import com.min.bunjang.category.repository.ThirdProductCategoryRepository;
import com.min.bunjang.config.ServiceTestConfig;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.product.dto.ProductCreateOrUpdateRequest;
import com.min.bunjang.product.dto.ProductSimpleResponse;
import com.min.bunjang.product.dto.ProductSimpleResponses;
import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductQualityState;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.model.Store;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ProductSearchServiceTest extends ServiceTestConfig {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FirstProductCategoryRepository firstProductCategoryRepository;
    @Autowired
    private SecondProductCategoryRepository secondProductCategoryRepository;
    @Autowired
    private ThirdProductCategoryRepository thirdProductCategoryRepository;

    @Autowired
    private ProductSearchService productSearchService;


    @DisplayName("키워드에 맞는 상품을 검색할수 있다.")
    @Test
    public void search_product_by_keyword() {
        //given\
        Member member = memberRepository.save(
                Member.createMember(MemberDirectCreateDto.of("email", "password", "name", "phone", null, MemberRole.ROLE_MEMBER, MemberGender.MEN)));
        Store store = storeRepository.save(Store.createStore("storeName", "intro", null, member));
        FirstProductCategory firstCategory = firstProductCategoryRepository.save(FirstProductCategory.createFirstProductCategory("firstCate"));
        SecondProductCategory secondCategory = secondProductCategoryRepository.save(SecondProductCategory.createSecondCategory("secondCate", firstCategory));
        ThirdProductCategory thirdCategory = thirdProductCategoryRepository.save(ThirdProductCategory.createThirdCategory("thirdCate", secondCategory));

        IntStream.range(0, 10).forEach(idx -> generateProduct("productNameV1" + idx, "exchangeLocationV1" + idx, store, firstCategory, secondCategory, thirdCategory));
        IntStream.range(10, 20).forEach(idx -> generateProduct("productNameV2" + idx, "exchangeLocationV2" + idx, store, firstCategory, secondCategory, thirdCategory));

        String keyword = "V2";

        //when
        ProductSimpleResponses productSimpleResponses = productSearchService.searchProductByKeyword(keyword, PageRequest.of(0, 20));

        //then
        Assertions.assertThat(productSimpleResponses.getPageDto().getTotal()).isEqualTo(10);
        Assertions.assertThat(productSimpleResponses.getPageDto().getCurrentPage()).isEqualTo(0);
        List<ProductSimpleResponse> productSimpleResponseList = productSimpleResponses.getProductSimpleResponses();
        Assertions.assertThat(productSimpleResponseList).hasSize(10);
    }

    private void generateProduct(
            String productName,
            String exchangeLocation,
            Store store,
            FirstProductCategory firstProductCategory,
            SecondProductCategory secondProductCategory,
            ThirdProductCategory thirdProductCategory
    ) {

        ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                null,
                null,
                productName,
                null,
                null,
                null,
                exchangeLocation,
                ProductQualityState.NEW_PRODUCT,
                ExchangeState.IMPOSSIBILITY,
                10000,
                DeliveryChargeInPrice.EXCLUDED,
                "product",
                new ArrayList<>(),
                1
        );
        productRepository.save(Product.createProduct(productCreateOrUpdateRequest, firstProductCategory, secondProductCategory, thirdProductCategory, store));
    }
}
