package com.min.bunjang.product.dto;

import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductQualityState;
import com.min.bunjang.product.model.ProductTradeState;
import com.min.bunjang.productinquire.dto.ProductInquireResponse;
import com.min.bunjang.store.dto.StoreSimpleResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductDetailResponse {
    private Long productNum;
    private Long firstCategoryNum;
    private Long secondCategoryNum;
    private Long thirdCategoryNum;
    private List<String> productPhotos;
    private String productName;
    private int productPrice;
    private int wishCount;
    private int hits;
    private LocalDateTime updateDateTime;
    private ProductTradeState productTradeState;
    private ProductQualityState productQualityState;
    private ExchangeState exchangeState;
    private DeliveryChargeInPrice deliveryChargeInPrice;
    private String exchangeLocation;
    private String productExplanation;
    private List<String> productTags;
    private List<ProductInquireResponse> productInquiries;

    private List<ProductSimpleResponse> productSimpleResponses;
    private StoreSimpleResponse storeSimpleResponse;

    public static ProductDetailResponse of(Product product, List<Product> productsByCategory) {
        return new ProductDetailResponse(
                product.getNum(),
                product.getFirstProductCategory().getNum(),
                product.getSecondProductCategory().getNum(),
                Optional.ofNullable(product.getThirdProductCategory().getNum()).orElse(null),
                null/* TODO 이거도 여러 사진으로 고쳐줘야함 일단 s3 도입후 적용*/,
                product.getProductName(),
                product.getProductPrice(),
                product.getWishProductCount(),
                product.getHits(),
                product.getUpdatedDate(),
                product.getProductTradeState(),
                product.getProductQualityState(),
                product.getExchangeState(),
                product.getDeliveryChargeInPrice(),
                product.getExchangeLocation(),
                product.getProductExplanation(),
                Optional.ofNullable(product.getProductTags()).orElse(null).stream()
                        .map(productTag -> productTag.getTag())
                        .collect(Collectors.toList()),
                Optional.ofNullable(product.getProductInquires()).orElse(null).stream()
                        .map(productInquire -> ProductInquireResponse.of(productInquire, product.checkAndReturnStore()))
                        .collect(Collectors.toList()),
                productsByCategory.stream()
                        .map(ProductSimpleResponse::of)
                        .collect(Collectors.toList()),
                StoreSimpleResponse.of(
                        product.checkAndReturnStore().getNum(),
                        product.checkAndReturnStore().getStoreName(),
                        product.checkAndReturnStore().getStoreThumbnail(),
                        product.checkAndReturnStore().getProducts().size(),
                        0 /* TODO 팔로워 기능 구현후 수정해야한다. */,
                        product.checkAndReturnStore().getStoreReviews().size()
                ));
    }

}
