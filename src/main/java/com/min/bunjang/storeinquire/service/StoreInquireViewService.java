package com.min.bunjang.storeinquire.service;

import com.min.bunjang.common.dto.PageDto;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.storeinquire.dto.StoreInquireListResponse;
import com.min.bunjang.storeinquire.dto.StoreInquireResponses;
import com.min.bunjang.storeinquire.model.StoreInquire;
import com.min.bunjang.storeinquire.repository.StoreInquireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreInquireViewService {
    private final StoreInquireRepository storeInquireRepository;

    public StoreInquireResponses findStoreInquiriesRelatedStore(Long storeNum, Pageable pageable) {
        Page<StoreInquire> storeInquiresByOwner = storeInquireRepository.findByOwnerNum(storeNum, pageable);
        return new StoreInquireResponses(
                StoreInquireListResponse.listOf(storeInquiresByOwner.getContent()),
                new PageDto(pageable.getPageSize(), pageable.getPageNumber(), storeInquiresByOwner.getTotalElements())
        );
    }

}
