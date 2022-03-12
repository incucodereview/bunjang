package com.min.bunjang.product.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.product.service.ProductViewService;
import com.min.bunjang.security.MemberAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductViewController {
    private final ProductViewService productViewService;

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @GetMapping(ProductViewControllerPath.PRODUCT_GET)
    public RestResponse<Void> getProduct(
            @PathVariable Long productNum,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        productViewService.getProduct(memberAccount.getEmail(), productNum);
        return RestResponse.of(HttpStatus.OK, null);
    }
}
