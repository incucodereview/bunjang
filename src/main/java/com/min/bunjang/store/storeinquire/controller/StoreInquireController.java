package com.min.bunjang.store.storeinquire.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.store.storeinquire.dto.InquireCreateRequest;
import com.min.bunjang.store.storeinquire.dto.InquireCreateResponse;
import com.min.bunjang.store.storeinquire.service.StoreInquireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreInquireController {
    private final StoreInquireService storeInquireService;

    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PostMapping(StoreInquireControllerPath.CREATE_INQUIRY)
    public RestResponse<InquireCreateResponse> createStoreInquiry(
            @Validated @RequestBody InquireCreateRequest inquireCreateRequest
    ) {
       return RestResponse.of(HttpStatus.CREATED, storeInquireService.createStoreInquiry(inquireCreateRequest));
    }
}
