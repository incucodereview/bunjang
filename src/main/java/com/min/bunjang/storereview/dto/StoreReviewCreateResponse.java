package com.min.bunjang.storereview.dto;

import com.min.bunjang.storereview.model.StoreReview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class StoreReviewCreateResponse {
    private Long writerNum;
    private String writerThumbnail;
    private String writerName;
    private double dealScore;
    private Long productNum;
    private String productName;
    private String reviewContent;

    public StoreReviewCreateResponse(Long writerNum, String writerThumbnail, String writerName, double dealScore, Long productNum, String productName, String reviewContent) {
        this.writerNum = writerNum;
        this.writerThumbnail = writerThumbnail;
        this.writerName = writerName;
        this.dealScore = dealScore;
        this.productNum = productNum;
        this.productName = productName;
        this.reviewContent = reviewContent;
    }

    public static StoreReviewCreateResponse of(StoreReview storeReview) {
        return new StoreReviewCreateResponse(
                storeReview.getWriterNum(),
                storeReview.getStoreThumbnail(),
                storeReview.getWriterName(),
                storeReview.getDealScore(),
                storeReview.getProductNum(),
                storeReview.getProductName(),
                storeReview.getReviewContent()
        );
    }
}
