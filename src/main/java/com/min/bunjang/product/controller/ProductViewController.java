package com.min.bunjang.product.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.product.dto.ProductDetailResponse;
import com.min.bunjang.product.dto.ProductSimpleResponse;
import com.min.bunjang.product.dto.ProductSimpleResponses;
import com.min.bunjang.product.service.ProductViewService;
import com.min.bunjang.security.MemberAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class ProductViewController {
    private final ProductViewService productViewService;

    @GetMapping(ProductViewControllerPath.PRODUCT_GET)
    public RestResponse<ProductDetailResponse> getProduct(
            @NotNull @PathVariable Long productNum,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        ProductDetailResponse productDetailResponse = productViewService.getProduct(productNum, memberAccount.getEmail());
        return RestResponse.of(HttpStatus.OK, productDetailResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @GetMapping(ProductViewControllerPath.PRODUCTS_FIND_BY_STORE)
    public RestResponse<ProductSimpleResponses> findProductsByStore(
            @NotNull @PathVariable Long storeNum,
            @AuthenticationPrincipal MemberAccount memberAccount,
            @PageableDefault(sort = "updatedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        ProductSimpleResponses productSimpleResponses = productViewService.findProductsByStore(memberAccount.getEmail(), storeNum, pageable);
        return RestResponse.of(HttpStatus.OK, productSimpleResponses);
    }
}
