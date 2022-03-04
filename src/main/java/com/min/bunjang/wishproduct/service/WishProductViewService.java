package com.min.bunjang.wishproduct.service;

import com.min.bunjang.common.dto.PageDto;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.wishproduct.dto.WishProductResponse;
import com.min.bunjang.wishproduct.dto.WishProductResponses;
import com.min.bunjang.wishproduct.model.WishProduct;
import com.min.bunjang.wishproduct.repository.WishProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishProductViewService {
    private final WishProductRepository wishProductRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public WishProductResponses findWishProductsByStore(Long storeNum, Pageable pageable) {
        Store store = storeRepository.findById(storeNum).orElseThrow(NotExistStoreException::new);
        Page<WishProduct> wishProductPages = wishProductRepository.findByStore(store, pageable);
        return new WishProductResponses(
                WishProductResponse.listOf(wishProductPages.getContent()),
                new PageDto(pageable.getPageSize(), pageable.getPageNumber(), wishProductPages.getTotalElements())
        );
    }
}
