package com.min.bunjang.store.dto.response;

import com.min.bunjang.aws.s3.dto.S3FileDto;
import com.min.bunjang.store.model.Store;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreSimpleResponse {
    private Long storeNum;
    private String storeName;
    private StoreThumbnailResponse storeThumbnail;
    private int productCount;
    private int followerCount;
    private int storeReviewCount;
//    private List<StoreReviewResponse> storeReviewResponses;

    public static StoreSimpleResponse of(Store store) {
        return new StoreSimpleResponse(
                store.getNum(),
                store.getStoreName(),
                //TODO StoreThumbnail 의 fileName 값에 대한 필요성을 다시 생각하고 필요 없다면 null을 어떻게 처리할지 -> S3FileDto을 변형하든 새로운 파일 DTO를 만들든. 애초에 파일에 이름이 필요한가..?
                StoreThumbnailResponse.of(store),
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
