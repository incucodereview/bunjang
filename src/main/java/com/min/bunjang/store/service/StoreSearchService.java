package com.min.bunjang.store.service;

import com.min.bunjang.store.dto.response.StoreSimpleResponse;
import com.min.bunjang.store.dto.response.StoreSimpleResponses;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreSearchService {
    private final StoreSearchRepository storeSearchRepository;

    @Transactional(readOnly = true)
    public StoreSimpleResponses searchStoreByKeyword(String keyword, Pageable pageable) {
        Page<Store> stores = storeSearchRepository.searchByKeyword(keyword, pageable);
        return new StoreSimpleResponses(StoreSimpleResponse.listOf(stores.getContent()));
    }
}
