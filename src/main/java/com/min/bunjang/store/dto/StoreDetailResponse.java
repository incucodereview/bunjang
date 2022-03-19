package com.min.bunjang.store.dto;

import com.min.bunjang.product.dto.ProductSimpleResponse;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.store.model.Store;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreDetailResponse {
    private Long storeNum;
    private String storeThumbnail;
    private String storeName;
    private double storeScore;
    private Period openDate;
    private int hits;
    private String introduceContent;
    private List<ProductSimpleResponse> productSimpleResponses;

    public static StoreDetailResponse of(Store store, List<Product> products) {
        return new StoreDetailResponse(
                store.getNum(),
                store.getStoreThumbnail(),
                store.getStoreName(),
                store.calculateAverageDealScore(),
                store.calculateOpenTime(),
                store.getHits(),
                store.getIntroduceContent(),
                products.stream()
                        .map(ProductSimpleResponse::of)
                        .collect(Collectors.toList())
        );
    }
}
