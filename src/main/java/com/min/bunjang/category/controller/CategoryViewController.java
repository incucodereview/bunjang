package com.min.bunjang.category.controller;

import com.min.bunjang.category.dto.AllCategoryListResponse;
import com.min.bunjang.category.service.CategoryViewService;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.product.dto.ProductSimpleResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class CategoryViewController {
    private final CategoryViewService categoryViewService;

    @GetMapping(CategoryViewControllerPath.CATEGORY_FIND_ALL)
    public RestResponse<AllCategoryListResponse> findAllCategories() {
        AllCategoryListResponse allCategoryListResponses = categoryViewService.findAllCategories();
        return RestResponse.of(HttpStatus.OK, allCategoryListResponses);
    }

    @GetMapping(CategoryViewControllerPath.CATEGORY_FIND_BY_FIRST)
    public RestResponse<ProductSimpleResponses> findProductsByFirstCategory(
            @NotNull @PathVariable Long firstCategoryNum,
            @PageableDefault(sort = "updateDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        ProductSimpleResponses productsByFirstCategory = categoryViewService.findProductsByFirstCategory(firstCategoryNum, pageable);
        return RestResponse.of(HttpStatus.OK, productsByFirstCategory);
    }

}
