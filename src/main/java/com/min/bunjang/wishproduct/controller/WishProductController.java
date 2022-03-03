package com.min.bunjang.wishproduct.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.wishproduct.dto.WishProductCreateRequest;
import com.min.bunjang.wishproduct.dto.WishProductsDeleteRequest;
import com.min.bunjang.wishproduct.service.WishProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping(WishProductControllerPath.WISH_PRODUCT_CREATE)
    public RestResponse<Void> createWishProduct(
            @Validated @RequestBody WishProductCreateRequest wishProductCreateRequest
    ) {
        wishProductService.createWishProduct(wishProductCreateRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @DeleteMapping(WishProductControllerPath.WISH_PRODUCT_DELETE)
    public RestResponse<Void> deleteWishProducts(
            @Validated @RequestBody WishProductsDeleteRequest wishProductsDeleteRequest
    ) {
        wishProductService.deleteWishProducts(wishProductsDeleteRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }
}
