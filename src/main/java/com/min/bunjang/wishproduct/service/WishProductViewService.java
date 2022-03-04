package com.min.bunjang.wishproduct.service;

import com.min.bunjang.common.dto.PageDto;
import com.min.bunjang.wishproduct.dto.WishProductResponse;
import com.min.bunjang.wishproduct.dto.WishProductResponses;
import com.min.bunjang.wishproduct.model.WishProduct;
import com.min.bunjang.wishproduct.repository.WishProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishProductViewService {
    private final WishProductRepository wishProductRepository;

    public WishProductResponses findWishProductsByStore(Long storeNum, Pageable pageable) {
        Page<WishProduct> wishProductPages = wishProductRepository.findByStoreNum(storeNum);
        return new WishProductResponses(
                WishProductResponse.listOf(wishProductPages.getContent()),
                new PageDto(pageable.getPageSize(), pageable.getPageNumber(), wishProductPages.getTotalElements())
        );
    }
}
