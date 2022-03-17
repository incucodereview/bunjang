package com.min.bunjang.storereview.dto.response;

import com.min.bunjang.storereview.model.StoreReview;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreReviewListResponse {
    private Long writerNum;
    private String writerName;
    private String writerThumbnail;
    private Long productNum;
    private String productName;
    private String reviewContent;
    private LocalDate postingDate;

    //TODO 왜만들었는지 기억이 안남;;;; 비슷한 응답 dto 존재함
//    public static StoreReviewListResponse of(StoreReview storeReview){
//        return new StoreReviewListResponse(
//                storeReview.getWriterNum(),
//                storeReview.getWriterName(),
//                storeReview.getStoreThumbnail(),
//                storeReview.getProductNum(),
//                storeReview.getProductName(),
//                storeReview.getReviewContent(),
//                storeReview.getUpdatedDate().toLocalDate()
//        );
//    }
//
//    public static List<StoreReviewListResponse> listOf(List<StoreReview> storeReviews) {
//        return storeReviews.stream()
//                .map(StoreReviewListResponse::of)
//                .collect(Collectors.toList());
//    }
}
