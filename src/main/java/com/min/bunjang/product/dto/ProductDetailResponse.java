package com.min.bunjang.product.dto;

import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductQualityState;
import com.min.bunjang.product.model.ProductTradeState;
import com.min.bunjang.productinquire.dto.ProductInquireResponse;
import com.min.bunjang.store.dto.response.StoreSimpleResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
    private List<ProductPhotoResponse> productPhotos;
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
                checkThirdCategoryForPreventionNPE(product),
                ProductPhotoResponse.listOf(product.getProductPhotos()),
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
                product.getProductTags().stream()
                        .map(productTag -> productTag.getTag())
                        .collect(Collectors.toList()),
                product.getProductInquires().stream()
                        .map(productInquire -> ProductInquireResponse.of(productInquire, product.checkAndReturnStore()))
                        .collect(Collectors.toList()),
                checkProductByCategoriesForPreventionNPE(productsByCategory),
                StoreSimpleResponse.of(product.checkAndReturnStore()));
    }

    private static Long checkThirdCategoryForPreventionNPE(Product product) {
        if (product.getThirdProductCategory() == null) {
            return null;
        }

        return product.getThirdProductCategory().getNum();
    }

    private static List<ProductSimpleResponse> checkProductByCategoriesForPreventionNPE(List<Product> productsByCategory) {
        if (productsByCategory == null) {
            return null;
        }

        return productsByCategory.stream()
                .map(ProductSimpleResponse::of)
                .collect(Collectors.toList());
    }

}
