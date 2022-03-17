package com.min.bunjang.storereview.dto.request;

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
    @NotNull
    private Long reviewNum;
    @NotNull
    private double updatedDealScore;
    @NotBlank
    private String updatedReviewContent;
}
