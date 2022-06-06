package com.min.bunjang.storereview.dto.request;

import com.min.bunjang.storereview.common.StoreReviewRequestValidatorMessages;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreReviewCreateRequest {
    @NotNull(message = StoreReviewRequestValidatorMessages.STORE_REVIEW_BLANK_OWNER_NUM)
    private Long ownerNum;
    @NotNull(message = StoreReviewRequestValidatorMessages.STORE_REVIEW_BLANK_WRITER_NUM)
    private Long writerNum;
    @NotNull(message = StoreReviewRequestValidatorMessages.STORE_REVIEW_BLANK_DEAL_SCORE)
    private double dealScore;
    @NotNull(message = StoreReviewRequestValidatorMessages.STORE_REVIEW_BLANK_PRODUCT_NUM)
    private Long productNum;
    @NotNull(message = StoreReviewRequestValidatorMessages.STORE_REVIEW_BLANK_CONTENT)
    private String reviewContent;
}
