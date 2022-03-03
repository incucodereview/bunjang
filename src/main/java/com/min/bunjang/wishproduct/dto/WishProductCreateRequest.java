package com.min.bunjang.wishproduct.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WishProductCreateRequest {
    @NotNull
    private Long storeNum;
    @NotNull
    private Long productNum;
}
