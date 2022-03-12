package com.min.bunjang.product.dto;

import com.min.bunjang.common.dto.PageDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductSimpleResponses {
    private List<ProductSimpleResponse> productSimpleResponses;
    private PageDto pageDto;
}
