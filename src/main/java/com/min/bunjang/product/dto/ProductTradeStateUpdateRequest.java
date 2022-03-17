package com.min.bunjang.product.dto;

import com.min.bunjang.product.model.ProductTradeState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductTradeStateUpdateRequest {
    @NotNull
    private ProductTradeState productTradeState;
}
