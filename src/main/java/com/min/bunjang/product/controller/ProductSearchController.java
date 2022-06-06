package com.min.bunjang.product.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.product.dto.response.ProductSimpleResponses;
import com.min.bunjang.product.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductSearchController {
    private final ProductSearchService productSearchService;

    @GetMapping(ProductSearchControllerPath.PRODUCT_SEARCH_BY_KEYWORD)
    public RestResponse<ProductSimpleResponses> searchProductByKeyword(
            @RequestParam("keyword") String keyword,
            @PageableDefault(sort = "num", direction = Sort.Direction.DESC, size = 100) Pageable pageable
    ) {
        ProductSimpleResponses productSimpleResponses = productSearchService.searchProductByKeyword(keyword, pageable);
        return RestResponse.of(HttpStatus.OK, productSimpleResponses);
    }
}
