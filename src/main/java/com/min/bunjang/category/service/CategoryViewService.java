package com.min.bunjang.category.service;

import com.min.bunjang.category.dto.AllCategoryListResponse;
import com.min.bunjang.category.dto.FirstProductCategoryResponse;
import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryViewService {
    private final FirstProductCategoryRepository firstProductCategoryRepository;

    @Transactional(readOnly = true)
    public AllCategoryListResponse findAllCategories() {
        List<FirstProductCategory> allCategories = firstProductCategoryRepository.findAllCategories();
        return new AllCategoryListResponse(FirstProductCategoryResponse.listOf(allCategories));
    }
}
