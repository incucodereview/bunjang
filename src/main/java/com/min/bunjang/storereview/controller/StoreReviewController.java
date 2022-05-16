package com.min.bunjang.storereview.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.storereview.dto.request.StoreReviewCreateRequest;
import com.min.bunjang.storereview.dto.response.StoreReviewResponse;
import com.min.bunjang.storereview.dto.request.StoreReviewUpdateRequest;
import com.min.bunjang.storereview.exception.NotExistStoreReviewException;
import com.min.bunjang.storereview.service.StoreReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
public class StoreReviewController {
    private final StoreReviewService storeReviewService;

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PostMapping(StoreReviewControllerPath.REVIEW_CREATE)
    public RestResponse<StoreReviewResponse> createStoreReview(
            @Validated @RequestBody StoreReviewCreateRequest storeReviewCreateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        StoreReviewResponse storeReview = storeReviewService.createStoreReview(memberAccount.getEmail(), storeReviewCreateRequest);
        return RestResponse.of(HttpStatus.OK, storeReview);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PutMapping(StoreReviewControllerPath.REVIEW_UPDATE)
    public RestResponse<Void> updateStoreReview(
            @Validated @RequestBody StoreReviewUpdateRequest storeReviewUpdateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        storeReviewService.updateStoreReview(memberAccount.getEmail(), storeReviewUpdateRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @DeleteMapping(StoreReviewControllerPath.REVIEW_DELETE)
    public RestResponse<Void> deleteStoreReview(
            @NotNull @PathVariable Long reviewNum,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
       storeReviewService.deleteStoreReview(memberAccount.getEmail(), reviewNum);
       return RestResponse.of(HttpStatus.OK, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistStoreException.class)
    public RestResponse<Void> notExistStoreExceptionHandler(NotExistStoreException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistProductException.class)
    public RestResponse<Void> notExistProductExceptionHandler(NotExistProductException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistStoreReviewException.class)
    public RestResponse<Void> notExistStoreReviewExceptionHandler(NotExistStoreReviewException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }
}
