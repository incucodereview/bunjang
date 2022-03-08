package com.min.bunjang.category.service;

import com.min.bunjang.category.dto.AllCategoryListResponses;
import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryViewService {
    private final FirstProductCategoryRepository firstProductCategoryRepository;

    @Transactional(readOnly = true)
    public AllCategoryListResponses findAllCategories() {
        return null;
    }
}
