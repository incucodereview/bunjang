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
public class StoreReviewUpdateRequest {
    @NotNull(message = StoreReviewRequestValidatorMessages.STORE_REVIEW_BLANK_NUM)
    private Long reviewNum;
    @NotNull(message = StoreReviewRequestValidatorMessages.STORE_REVIEW_BLANK_DEAL_SCORE)
    private double updatedDealScore;
    @NotBlank(message = StoreReviewRequestValidatorMessages.STORE_REVIEW_BLANK_CONTENT)
    private String updatedReviewContent;
}
