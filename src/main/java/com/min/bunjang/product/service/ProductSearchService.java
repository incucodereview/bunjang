package com.min.bunjang.product.service;

import com.min.bunjang.common.dto.PageDto;
import com.min.bunjang.product.dto.ProductSimpleResponse;
import com.min.bunjang.product.dto.ProductSimpleResponses;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductSearchRepository productSearchRepository;

    @Transactional
    public ProductSimpleResponses searchProductByKeyword(String keyword, Pageable pageable) {
        Page<Product> products = productSearchRepository.searchByKeyword(keyword, pageable);

        return new ProductSimpleResponses(
                ProductSimpleResponse.listOf(products.getContent()),
                new PageDto(pageable.getPageSize(), pageable.getPageNumber(), products.getTotalElements())
        );
    }
}
