package com.min.bunjang.wishproduct.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.wishproduct.dto.WishProductCreateRequest;
import com.min.bunjang.wishproduct.dto.WishProductsDeleteRequest;
import com.min.bunjang.wishproduct.service.WishProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class WishProductController {
    private final WishProductService wishProductService;

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PostMapping(WishProductControllerPath.WISH_PRODUCT_CREATE)
    public RestResponse<Void> createWishProduct(
            @Validated @RequestBody WishProductCreateRequest wishProductCreateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        wishProductService.createWishProduct(memberAccount.getEmail(), wishProductCreateRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @DeleteMapping(WishProductControllerPath.WISH_PRODUCT_DELETE)
    public RestResponse<Void> deleteWishProducts(
            @Validated @RequestBody WishProductsDeleteRequest wishProductsDeleteRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        wishProductService.deleteWishProducts(memberAccount.getEmail(), wishProductsDeleteRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }
}
