package com.min.bunjang.wishproduct.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.wishproduct.dto.response.WishProductResponses;
import com.min.bunjang.wishproduct.service.WishProductViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class WishProductViewController {
    private final WishProductViewService wishProductViewService;

    @GetMapping(WishProductViewControllerPath.WISH_PRODUCT_FIND_BY_STORE)
    public RestResponse<WishProductResponses> findWishProductsByStore(
            @NotNull @PathVariable Long storeNum,
            @AuthenticationPrincipal MemberAccount memberAccount,
            @PageableDefault(sort = "num", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        WishProductResponses wishProductsByStore = wishProductViewService.findWishProductsByStore(memberAccount.getEmail(), storeNum, pageable);
        return RestResponse.of(HttpStatus.OK, wishProductsByStore);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistStoreException.class)
    public RestResponse<Void> notExistStoreExceptionHandler(NotExistStoreException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }
}
