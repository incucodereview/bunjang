package com.min.bunjang.store.dto;

import com.min.bunjang.store.model.Store;
import com.min.bunjang.storereview.dto.response.StoreReviewResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreSimpleResponse {
    private Long storeNum;
    private String storeName;
    private String storeThumbnail;
    private int productCount;
    private int followerCount;
    private int storeReviewCount;
//    private List<StoreReviewResponse> storeReviewResponses;

    public static StoreSimpleResponse of(Store store) {
        return new StoreSimpleResponse(
                store.getNum(),
                store.getStoreName(),
                store.getStoreThumbnail(),
                store.getProducts().size(),
                store.getFollowers().size(),
                store.getStoreReviews().size()
        );
    }

    public static List<StoreSimpleResponse> listOf(List<Store> stores) {
        return stores.stream()
                .map(StoreSimpleResponse::of)
                .collect(Collectors.toList());
    }
}
