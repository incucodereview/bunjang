package com.min.bunjang.product.dto;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductQualityState;
import com.min.bunjang.store.model.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ProductDetailResponseTest {

    @DisplayName("ProductDetailResponse of로 생성시 널포인터 발생 테스트")
    @Test
    public void createTest() {
        //given
        ProductCreateOrUpdateRequest productCreateOrUpdateRequest = new ProductCreateOrUpdateRequest(
                1L,
                null,
                "productName",
                null,
                null,
                null,
                "seoul",
                ProductQualityState.NEW_PRODUCT,
                ExchangeState.IMPOSSIBILITY,
                100000,
                DeliveryChargeInPrice.EXCLUDED,
                "제품 설명 입니다.",
                Arrays.asList("tag1", "tag2"),
                1
        );
        FirstProductCategory first = FirstProductCategory.createFirstProductCategory("first");
        SecondProductCategory second = SecondProductCategory.createSecondCategory("second", first);

        Store store = Store.createStore("store", "intro",  null, null);

        Product product = Product.createProduct(productCreateOrUpdateRequest, first, second, null, store);

        //when
        ProductDetailResponse productDetailResponse = ProductDetailResponse.of(product, new ArrayList<>());
        //then
    }

}