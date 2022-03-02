package com.min.bunjang.storereview.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.storereview.dto.StoreReviewCreateRequest;
import com.min.bunjang.storereview.dto.StoreReviewCreateResponse;
import com.min.bunjang.storereview.service.StoreReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreReviewController {
    private final StoreReviewService storeReviewService;

    @PostMapping(StoreReviewControllerPath.REVIEW_CREATE)
    public RestResponse<StoreReviewCreateResponse> createStoreReview(
            @Validated @RequestBody StoreReviewCreateRequest storeReviewCreateRequest
    ) {
        StoreReviewCreateResponse storeReview = storeReviewService.createStoreReview(storeReviewCreateRequest);
        return RestResponse.of(HttpStatus.OK, storeReview);
    }
}
