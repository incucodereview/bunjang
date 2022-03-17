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
public class StoreReviewCreateRequest {
    @NotNull
    private Long ownerNum;
    @NotNull
    private double dealScore;
    @NotNull
    private Long productNum;
    @NotBlank
    private String reviewContent;
}
