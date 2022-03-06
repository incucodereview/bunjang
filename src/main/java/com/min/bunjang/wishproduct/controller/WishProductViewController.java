package com.min.bunjang.wishproduct.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.wishproduct.service.WishProductViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class WishProductViewController {
    private final WishProductViewService wishProductViewService;

    @GetMapping(WishProductViewControllerPath.WISH_PRODUCT_FIND_BY_STORE)
    public RestResponse<Void> findWishProductsByStore(
            @NotNull @PathVariable Long storeNum,
            @AuthenticationPrincipal MemberAccount memberAccount,
            @PageableDefault(sort = "num", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        wishProductViewService.findWishProductsByStore(memberAccount.getEmail(), storeNum, pageable);
        return RestResponse.of(HttpStatus.OK, null);
    }
}
