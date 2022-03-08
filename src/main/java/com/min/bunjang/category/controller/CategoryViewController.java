package com.min.bunjang.category.controller;

import com.min.bunjang.category.dto.AllCategoryListResponses;
import com.min.bunjang.category.service.CategoryViewService;
import com.min.bunjang.common.dto.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryViewController {
    private final CategoryViewService categoryViewService;

    @GetMapping(CategoryViewControllerPath.CATEGORY_FIND_ALL)
    public RestResponse<AllCategoryListResponses> findAllCategories() {
        AllCategoryListResponses allCategoryListResponses = categoryViewService.findAllCategories();
        return RestResponse.of(HttpStatus.OK, allCategoryListResponses);
    }
}
