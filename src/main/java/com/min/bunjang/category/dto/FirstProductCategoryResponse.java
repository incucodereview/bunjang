package com.min.bunjang.category.dto;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FirstProductCategoryResponse {
    private Long categoryNum;
    private String categoryName;
    private List<SecondProductCategoryResponse> secondProductCategoryResponseList;

    public static FirstProductCategoryResponse of(FirstProductCategory firstProductCategory) {
        return new FirstProductCategoryResponse(
                firstProductCategory.getNum(),
                firstProductCategory.getCategoryName(),
                SecondProductCategoryResponse.listOf(firstProductCategory.getSecondProductCategories())
        );
    }

    public static List<FirstProductCategoryResponse> listOf(List<FirstProductCategory> firstProductCategories) {
        return firstProductCategories.stream()
                .map(FirstProductCategoryResponse::of)
                .sorted(Comparator.comparing(FirstProductCategoryResponse::getCategoryNum))
                .collect(Collectors.toList());
    }

}
