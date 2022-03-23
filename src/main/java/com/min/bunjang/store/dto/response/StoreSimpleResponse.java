package com.min.bunjang.store.dto.response;

import com.min.bunjang.aws.s3.dto.S3FileDto;
import com.min.bunjang.store.model.Store;
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
    private S3FileDto storeThumbnail;
    private int productCount;
    private int followerCount;
    private int storeReviewCount;
//    private List<StoreReviewResponse> storeReviewResponses;

    public static StoreSimpleResponse of(Store store) {
        return new StoreSimpleResponse(
                store.getNum(),
                store.getStoreName(),
                new S3FileDto(store.getStoreThumbnail().getNum(), store.getStoreThumbnail().getFileName(), store.getStoreThumbnail().getFilePath()),
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
