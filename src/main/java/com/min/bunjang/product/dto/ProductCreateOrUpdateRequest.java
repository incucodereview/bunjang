package com.min.bunjang.product.dto;

import com.min.bunjang.product.model.DeliveryChargeInPrice;
import com.min.bunjang.product.model.ExchangeState;
import com.min.bunjang.product.model.ProductState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductCreateOrUpdateRequest {
    @NotNull
    private Long storeNum;
    private List<String> productPhotos;
    @NotBlank
    private String productName;
    @NotNull
    private Long firstCategoryNum;
    @NotNull
    private Long secondCategoryNum;
    private Long thirdCategoryNum;
    @NotNull
    private String exchangeLocation;
    @NotNull
    private ProductState productState;
    @NotNull
    private ExchangeState exchangeState;
    @NotNull
    private int productPrice;
    @NotNull
    private DeliveryChargeInPrice deliveryChargeInPrice;
    @NotNull
    private String productExplanation;
    private List<String> tags;
    private int productAmount;

}
