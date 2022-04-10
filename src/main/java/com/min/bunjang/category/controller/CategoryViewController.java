package com.min.bunjang.category.controller;

import com.min.bunjang.category.dto.response.AllCategoryListResponse;
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
            @PageableDefault(sort = "updatedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        ProductSimpleResponses productsByFirstCategory = categoryViewService.findProductsByFirstCategory(firstCategoryNum, pageable);
        return RestResponse.of(HttpStatus.OK, productsByFirstCategory);
    }

    @GetMapping(CategoryViewControllerPath.CATEGORY_FIND_BY_SECOND)
    public RestResponse<ProductSimpleResponses> findProductsBySecondCategory(
            @NotNull @PathVariable Long secondCategoryNum,
            @PageableDefault(sort = "updatedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        ProductSimpleResponses productsBySecondCategory = categoryViewService.findProductsBySecondCategory(secondCategoryNum, pageable);
        return RestResponse.of(HttpStatus.OK, productsBySecondCategory);
    }

    @GetMapping(CategoryViewControllerPath.CATEGORY_FIND_BY_THIRD)
    public RestResponse<ProductSimpleResponses> findProductsByThirdCategory(
            @NotNull @PathVariable Long thirdCategoryNum,
            @PageableDefault(sort = "updatedDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        ProductSimpleResponses productsByThirdCategory = categoryViewService.findProductsByThirdCategory(thirdCategoryNum, pageable);
        return RestResponse.of(HttpStatus.OK, productsByThirdCategory);
    }

}
