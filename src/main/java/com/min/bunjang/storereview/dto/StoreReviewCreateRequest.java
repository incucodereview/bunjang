package com.min.bunjang.storereview.dto;

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
    private Long writerNum;
    @NotNull
    private double dealScore;
    @NotNull
    private Long productNum;
    @NotBlank
    private String productName;
    @NotBlank
    private String reviewContent;
}
