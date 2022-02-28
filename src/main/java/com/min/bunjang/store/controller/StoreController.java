package com.min.bunjang.store.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.store.dto.StoreCreateRequest;
import com.min.bunjang.store.dto.StoreCreateResponse;
import com.min.bunjang.store.dto.StoreIntroduceDto;
import com.min.bunjang.store.dto.VisitorPlusDto;
import com.min.bunjang.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PostMapping(StoreControllerPath.STORE_CREATE)
    public RestResponse<StoreCreateResponse> createStore(@Validated @RequestBody StoreCreateRequest storeCreateRequest) {
        StoreCreateResponse storeCreateResponse = storeService.createStore(storeCreateRequest);
        return RestResponse.of(HttpStatus.OK, storeCreateResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PutMapping(StoreControllerPath.STORE_INTRODUCE_CONTENT_UPDATE)
    public RestResponse<Void> updateIntroduceContent(@Validated @RequestBody StoreIntroduceDto storeIntroduceDto) {
        storeService.updateIntroduceContent(storeIntroduceDto);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @PostMapping(StoreControllerPath.STORE_INTRODUCE_CONTENT_UPDATE)
    public RestResponse<Void> plusVisitor(@Validated @RequestBody VisitorPlusDto visitorPlusDto) {
        storeService.plusVisitor(visitorPlusDto);
        return RestResponse.of(HttpStatus.OK, null);
    }

}
