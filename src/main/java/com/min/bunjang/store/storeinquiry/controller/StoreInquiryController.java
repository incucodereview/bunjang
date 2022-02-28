package com.min.bunjang.store.storeinquiry.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.store.storeinquiry.dto.InquiryCreateRequest;
import com.min.bunjang.store.storeinquiry.dto.InquiryCreateResponse;
import com.min.bunjang.store.storeinquiry.model.StoreInquiry;
import com.min.bunjang.store.storeinquiry.service.StoreInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreInquiryController {
    private final StoreInquiryService storeInquiryService;

    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PostMapping(StoreInquiryControllerPath.CREATE_INQUIRY)
    public RestResponse<InquiryCreateResponse> createStoreInquiry(
            @Validated @RequestBody InquiryCreateRequest inquiryCreateRequest
    ) {
       return RestResponse.of(HttpStatus.CREATED, storeInquiryService.createStoreInquiry(inquiryCreateRequest));
    }
}
