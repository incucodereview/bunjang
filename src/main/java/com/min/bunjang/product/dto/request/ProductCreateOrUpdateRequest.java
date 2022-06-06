package com.min.bunjang.product.dto.request;

import com.min.bunjang.product.common.ProductRequestValidatorMessages;
import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductQualityState;
import com.min.bunjang.product.model.ProductTag;
import com.min.bunjang.product.model.ProductTradeState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductCreateOrUpdateRequest {
    @NotNull(message = ProductRequestValidatorMessages.PRODUCT_BLANK_STORE_NUM)
    private Long storeNum;
    private List<String> productPhotos;
    @NotBlank(message = ProductRequestValidatorMessages.PRODUCT_BLANK_NAME)
    private String productName;
    @NotNull(message = ProductRequestValidatorMessages.PRODUCT_BLANK_FIRST_CATEGORY)
    private Long firstCategoryNum;
    @NotNull(message = ProductRequestValidatorMessages.PRODUCT_BLANK_SECOND_CATEGORY)
    private Long secondCategoryNum;
    private Long thirdCategoryNum;
    @NotNull(message = ProductRequestValidatorMessages.PRODUCT_BLANK_TRADE_LOCATION)
    private String tradeLocation;
    @NotNull(message = ProductRequestValidatorMessages.PRODUCT_BLANK_QUALITY_STATE)
    private ProductQualityState productQualityState;
    @NotNull(message = ProductRequestValidatorMessages.PRODUCT_BLANK_EXCHANGE_STATE)
    private ExchangeState exchangeState;
    @NotNull(message = ProductRequestValidatorMessages.PRODUCT_BLANK_PRICE)
    private int productPrice;
    @NotNull(message = ProductRequestValidatorMessages.PRODUCT_BLANK_DELIVERY_CHARGE_IN_PRICE)
    private DeliveryChargeInPrice deliveryChargeInPrice;
    @NotNull(message = ProductRequestValidatorMessages.PRODUCT_BLANK_EXPLANATION)
    private String productExplanation;
    private List<String> tags;
    private int productAmount;

    public List<ProductTag> makeProductTags(Product product) {
        return this.tags.stream()
                .map(tag -> ProductTag.createProductTag(tag, product))
                .collect(Collectors.toList());
    }
}
