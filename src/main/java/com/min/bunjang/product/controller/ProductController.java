package com.min.bunjang.product.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.product.dto.ProductCreateRequest;
import com.min.bunjang.product.service.ProductService;
import com.min.bunjang.security.MemberAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(ProductControllerPath.PRODUCT_CREATE)
    public RestResponse<Void> createProduct(
            @Validated @RequestBody ProductCreateRequest productCreateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        productService.createProduct(memberAccount.getEmail(), productCreateRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }
}
