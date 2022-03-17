package com.min.bunjang.store.dto;

import com.min.bunjang.storereview.dto.response.StoreReviewResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    public static StoreSimpleResponse of(
            Long storeNum,
            String storeName,
            String storeThumbnail,
            int productCount,
            int followerCount,
            int storeReviewCount
//            List<StoreReviewResponse> storeReviewResponses
    ) {
        return new StoreSimpleResponse(
                storeNum,
                storeName,
                storeThumbnail,
                productCount,
                followerCount,
                storeReviewCount
//                storeReviewResponses
        );
    }
}
