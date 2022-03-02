package com.min.bunjang.storereview.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreReviewUpdateRequest {
    @NotNull
    private Long reviewNum;
    @NotBlank
    private String updatedReviewContent;
}
