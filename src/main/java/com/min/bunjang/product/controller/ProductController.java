package com.min.bunjang.product.controller;

import com.min.bunjang.category.exception.NotExistProductCategoryException;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.product.dto.request.ProductCreateOrUpdateRequest;
import com.min.bunjang.product.dto.request.ProductDeleteRequest;
import com.min.bunjang.product.dto.request.ProductTradeStateUpdateRequest;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.product.service.ProductService;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.exception.NotExistStoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PostMapping(ProductControllerPath.PRODUCT_CREATE)
    public RestResponse<Void> createProduct(
            @Validated @RequestBody ProductCreateOrUpdateRequest productCreateOrUpdateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        productService.createProduct(memberAccount.getEmail(), productCreateOrUpdateRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PutMapping(ProductControllerPath.PRODUCT_UPDATE)
    public RestResponse<Void> updateProduct(
            @NotNull @PathVariable Long productNum,
            @Validated @RequestBody ProductCreateOrUpdateRequest productCreateOrUpdateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        productService.updateProduct(memberAccount.getEmail(), productNum, productCreateOrUpdateRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @DeleteMapping(ProductControllerPath.PRODUCT_DELETE)
    public RestResponse<Void> deleteProduct(
            @Validated @RequestBody ProductDeleteRequest productDeleteRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        productService.deleteProduct(memberAccount.getEmail(), productDeleteRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PatchMapping(ProductControllerPath.PRODUCT_UPDATE_TRADE_STATE)
    public RestResponse<Void> updateProductTradeState(
            @NotNull @PathVariable Long productNum,
            @Validated @RequestBody ProductTradeStateUpdateRequest productTradeStateUpdateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        productService.updateProductTradeState(memberAccount.getEmail(), productNum, productTradeStateUpdateRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    /////// exception handler ///////

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistProductException.class)
    public RestResponse<Void> notExistProductExceptionHandler(NotExistProductException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistProductCategoryException.class)
    public RestResponse<Void> notExistProductCategoryExceptionHandler(NotExistProductCategoryException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistStoreException.class)
    public RestResponse<Void> notExistStoreExceptionHandler(NotExistStoreException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }
}
