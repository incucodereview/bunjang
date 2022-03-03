package com.min.bunjang.storereview.service;

import com.min.bunjang.common.dto.PageDto;
import com.min.bunjang.storereview.dto.StoreReviewListResponse;
import com.min.bunjang.storereview.dto.StoreReviewListResponses;
import com.min.bunjang.storereview.model.StoreReview;
import com.min.bunjang.storereview.repository.StoreReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreReviewViewService {
    private final StoreReviewRepository storeReviewRepository;

    @Transactional(readOnly = true)
    public StoreReviewListResponses findStoreReviewsByStore(Long storeNum, Pageable pageable) {
        Page<StoreReview> reviewPages = storeReviewRepository.findByOwnerNum(storeNum, pageable);
        return new StoreReviewListResponses(
                StoreReviewListResponse.listOf(reviewPages.getContent()),
                new PageDto(pageable.getPageSize(), pageable.getPageNumber(), reviewPages.getTotalElements())
        );
    }
}
