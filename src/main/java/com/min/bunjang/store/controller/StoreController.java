package com.min.bunjang.store.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.dto.request.StoreCreateOrUpdateRequest;
import com.min.bunjang.store.dto.response.StoreCreateResponse;
import com.min.bunjang.store.dto.request.StoreIntroduceUpdateRequest;
import com.min.bunjang.store.dto.request.StoreNameUpdateRequest;
import com.min.bunjang.store.dto.request.VisitorPlusDto;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PostMapping(StoreControllerPath.STORE_CREATE)
    public RestResponse<StoreCreateResponse> createStore(
            @Validated @RequestBody StoreCreateOrUpdateRequest storeCreateOrUpdateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) throws IOException {
        StoreCreateResponse storeCreateResponse = storeService.createStore(storeCreateOrUpdateRequest, memberAccount.getEmail());
        return RestResponse.of(HttpStatus.OK, storeCreateResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PatchMapping(StoreControllerPath.STORE_INTRODUCE_CONTENT_UPDATE)
    public RestResponse<Void> updateIntroduceContent(
            @Validated @RequestBody StoreIntroduceUpdateRequest storeIntroduceUpdateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        storeService.updateIntroduceContent(memberAccount.getEmail(), storeIntroduceUpdateRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PatchMapping(StoreControllerPath.STORE_NAME_UPDATE)
    public RestResponse<Void> updateStoreName(
            @Validated @RequestBody StoreNameUpdateRequest storeNameUpdateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        storeService.updateStoreName(storeNameUpdateRequest, memberAccount.getEmail());
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PostMapping(StoreControllerPath.STORE_PLUS_VISITOR)
    public RestResponse<Void> plusVisitor(
            @Validated @RequestBody VisitorPlusDto visitorPlusDto,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        storeService.plusVisitor(visitorPlusDto, memberAccount.getEmail());
        return RestResponse.of(HttpStatus.OK, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistMemberException.class)
    public RestResponse<Void> notExistMemberExceptionHandler(NotExistMemberException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistStoreException.class)
    public RestResponse<Void> notExistStoreExceptionHandler(NotExistStoreException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }
}
