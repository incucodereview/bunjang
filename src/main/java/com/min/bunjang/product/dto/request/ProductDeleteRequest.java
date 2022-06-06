package com.min.bunjang.product.dto.request;

import com.min.bunjang.product.common.ProductRequestValidatorMessages;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductDeleteRequest {
    @NotNull(message = ProductRequestValidatorMessages.PRODUCT_BLANK_NUM)
    private Long productNum;
    @NotNull(message = ProductRequestValidatorMessages.PRODUCT_BLANK_STORE_NUM)
    private Long storeNum;
}
