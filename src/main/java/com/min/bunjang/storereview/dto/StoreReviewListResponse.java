package com.min.bunjang.storereview.dto;

import com.min.bunjang.storereview.model.StoreReview;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreReviewListResponse {
    private Long writerNum;
    private String writerName;
    private Long productNum;
    private String productName;
    private String reviewContent;
    private LocalDate postingDate;

    public StoreReviewListResponse(Long writerNum, String writerName, Long productNum, String productName, String reviewContent, LocalDate postingDate) {
        this.writerNum = writerNum;
        this.writerName = writerName;
        this.productNum = productNum;
        this.productName = productName;
        this.reviewContent = reviewContent;
        this.postingDate = postingDate;
    }

    public static StoreReviewListResponse of(StoreReview storeReview){
        return new StoreReviewListResponse(
                storeReview.getWriterNum(),
                storeReview.getWriterName(),
                storeReview.getProductNum(),
                storeReview.getProductName(),
                storeReview.getReviewContent(),
                storeReview.getUpdatedDate().toLocalDate()
        );
    }

    public static List<StoreReviewListResponse> listOf(List<StoreReview> storeReviews) {
        return storeReviews.stream()
                .map(StoreReviewListResponse::of)
                .collect(Collectors.toList());
    }
}
