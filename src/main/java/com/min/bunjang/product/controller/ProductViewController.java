package com.min.bunjang.product.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.common.validator.RightRequesterChecker;
import com.min.bunjang.product.dto.response.ProductDetailResponse;
import com.min.bunjang.product.dto.response.ProductSimpleResponses;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.service.ProductViewService;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.exception.NotExistStoreException;
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
public class ProductViewController {
    private final ProductViewService productViewService;

    @GetMapping(ProductViewControllerPath.PRODUCT_GET)
    public RestResponse<ProductDetailResponse> getProduct(
            @NotNull @PathVariable Long productNum,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        ProductDetailResponse productDetailResponse = productViewService.getProduct(productNum, memberAccount);
        return RestResponse.of(HttpStatus.OK, productDetailResponse);
    }

    @GetMapping(ProductViewControllerPath.PRODUCTS_FIND_BY_STORE)
    public RestResponse<ProductSimpleResponses> findProductsByStore(
            @NotNull @PathVariable Long storeNum,
            @AuthenticationPrincipal MemberAccount memberAccount,
            @PageableDefault(sort = "updatedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        ProductSimpleResponses productSimpleResponses = productViewService.findProductsByStore(memberAccount, storeNum, pageable);
        return RestResponse.of(HttpStatus.OK, productSimpleResponses);
    }

    ////// exception handlers //////
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistProductException.class)
    public RestResponse<Void> notExistProductExceptionHandler(NotExistProductException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() +Arrays.asList(e.getStackTrace()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistStoreException.class)
    public RestResponse<Void> notExistStoreExceptionHandler(NotExistStoreException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }
}
