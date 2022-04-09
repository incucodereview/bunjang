package com.min.bunjang.storeinquire.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.join.confirmtoken.exception.WrongConfirmEmailToken;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.storeinquire.dto.InquireCreateRequest;
import com.min.bunjang.storeinquire.dto.InquireCreateResponse;
import com.min.bunjang.storeinquire.exception.NotExistStoreInquireException;
import com.min.bunjang.storeinquire.service.StoreInquireService;
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
public class 가 {
    private final StoreInquireService storeInquireService;

    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PostMapping(StoreInquireControllerPath.CREATE_INQUIRY)
    public RestResponse<InquireCreateResponse> createStoreInquiry(
            @Validated @RequestBody InquireCreateRequest inquireCreateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        return RestResponse.of(HttpStatus.CREATED, storeInquireService.createStoreInquiry(memberAccount.getEmail(), inquireCreateRequest));
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @DeleteMapping(StoreInquireControllerPath.DELETE_INQUIRY)
    public RestResponse<Void> deleteStoreInquire(
            @NotNull @PathVariable Long inquireNum,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        storeInquireService.deleteStoreInquire(memberAccount.getEmail(), inquireNum);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistStoreException.class)
    public RestResponse<Void> notExistStoreExceptionHandler(WrongConfirmEmailToken e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistStoreInquireException.class)
    public RestResponse<Void> notExistStoreInquireExceptionHandler(WrongConfirmEmailToken e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }
}
