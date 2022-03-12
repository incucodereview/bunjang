package com.min.bunjang.product.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductDetailResponse {
    private Long productNum;
    private Long storeNum;
    private Long firstCategoryNum;
    private Long secondCategoryNum;
    private Long thirdCategoryNum;
    private List<String> productPhotos;
    private String productName;
    private int productPrice;
    private int wishCount;
    private int hits;
    private LocalDateTime createDateTime;
    private String productState;
    private String exchangeState;
    private String deliveryChargeInPrice;
    private String exchangeLocation;

    private String productExplanation;
    private List<String> productTags;
    private List<String> productInquiries;

}
