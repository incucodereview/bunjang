package com.min.bunjang.category.service;

import com.min.bunjang.category.dto.AllCategoryListResponse;
import com.min.bunjang.category.dto.FirstProductCategoryResponse;
import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.common.dto.PageDto;
import com.min.bunjang.product.dto.ProductSimpleResponse;
import com.min.bunjang.product.dto.ProductSimpleResponses;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryViewService {
    private final FirstProductCategoryRepository firstProductCategoryRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public AllCategoryListResponse findAllCategories() {
        List<FirstProductCategory> allCategories = firstProductCategoryRepository.findAllCategories();
        return new AllCategoryListResponse(FirstProductCategoryResponse.listOf(allCategories));
    }

    public ProductSimpleResponses findProductsByFirstCategory(Long firstCategoryNum, Pageable pageable) {
        Page<Product> firstProductCategories = productRepository.findByFirstProductCategoryNum(firstCategoryNum, pageable);
        List<ProductSimpleResponse> productSimpleResponseList = ProductSimpleResponse.listOf(firstProductCategories.getContent());
        return new ProductSimpleResponses(
                productSimpleResponseList,
                new PageDto(pageable.getPageSize(), pageable.getPageNumber(), firstProductCategories.getTotalElements())
        );
    }
}
