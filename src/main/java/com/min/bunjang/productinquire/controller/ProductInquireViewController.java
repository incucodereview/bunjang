package com.min.bunjang.productinquire.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.productinquire.dto.response.ProductInquireResponses;
import com.min.bunjang.productinquire.service.ProductInquireViewService;
import com.min.bunjang.store.exception.NotExistStoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class ProductInquireViewController {
    private final ProductInquireViewService productInquireViewService;

    @GetMapping(ProductInquireViewControllerPath.PRODUCT_INQUIRE_FIND_BY_PRODUCT)
    public RestResponse<ProductInquireResponses> findProductInquireByProduct(
            @PathVariable Long productNum,
            @PageableDefault(sort = "updatedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        ProductInquireResponses productInquireResponses = productInquireViewService.findProductInquireByProduct(productNum, pageable);
        return RestResponse.of(HttpStatus.OK, productInquireResponses);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistStoreException.class)
    public RestResponse<Void> notExistStoreExceptionHandler(NotExistStoreException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }
}
