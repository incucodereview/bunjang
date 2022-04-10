package com.min.bunjang.productinquire.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.productinquire.dto.request.ProductInquireCreateRequest;
import com.min.bunjang.productinquire.exception.NotExistProductInquireException;
import com.min.bunjang.productinquire.service.ProductInquireService;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.exception.NotExistStoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class ProductInquireController {
    private final ProductInquireService productInquireService;

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PostMapping(ProductInquireControllerPath.PRODUCT_INQUIRE_CREATE)
    public RestResponse<Void> createProductInquire(
            @Validated @RequestBody ProductInquireCreateRequest productInquireCreateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        productInquireService.createProductInquire(memberAccount.getEmail(), productInquireCreateRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @DeleteMapping(ProductInquireControllerPath.PRODUCT_INQUIRE_DELETE)
    public RestResponse<Void> deleteProductInquire(
            @NotNull @PathVariable Long inquireNum,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        productInquireService.deleteProductInquire(memberAccount.getEmail(), inquireNum);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistProductException.class)
    public RestResponse<Void> notExistProductExceptionHandler(NotExistProductException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistStoreException.class)
    public RestResponse<Void> notExistStoreExceptionHandler(NotExistStoreException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistProductInquireException.class)
    public RestResponse<Void> notExistProductInquireExceptionHandler(NotExistProductInquireException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }
}
