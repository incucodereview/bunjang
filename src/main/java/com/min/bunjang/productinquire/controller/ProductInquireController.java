package com.min.bunjang.productinquire.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.productinquire.dto.ProductInquireCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductInquireController {
    private final ProductInquireService productInquireService;

    @PostMapping(ProductInquireControllerPath.PRODUCT_INQUIRE_CREATE)
    public RestResponse<Void> createProductInquire(
            @Validated @RequestBody ProductInquireCreateRequest productInquireCreateRequest
    ) {
        return RestResponse.of(HttpStatus.OK, null);
    }
}
