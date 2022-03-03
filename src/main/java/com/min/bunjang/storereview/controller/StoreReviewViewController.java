package com.min.bunjang.storereview.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.storereview.dto.StoreReviewListResponse;
import com.min.bunjang.storereview.dto.StoreReviewListResponses;
import com.min.bunjang.storereview.model.StoreReview;
import com.min.bunjang.storereview.service.StoreReviewViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class StoreReviewViewController {
    private final StoreReviewViewService storeReviewViewService;

    @GetMapping(StoreReviewViewControllerPath.REVIEW_FIND_BY_STORE)
    public RestResponse<StoreReviewListResponses> findStoreReviewsByStore(
            @NotNull @PathVariable Long storeNum,
            @PageableDefault(sort = "num", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        StoreReviewListResponses reviewsByStore = storeReviewViewService.findStoreReviewsByStore(storeNum, pageable);
        return RestResponse.of(HttpStatus.OK, reviewsByStore);
    }

}
