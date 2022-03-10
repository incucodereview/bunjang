package com.min.bunjang.category.dto;

import com.min.bunjang.category.model.ThirdProductCategory;
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
public class ThirdProductCategoryResponse {
    private Long categoryNum;
    private String categoryName;

    public static ThirdProductCategoryResponse of(ThirdProductCategory thirdProductCategory) {
        return new ThirdProductCategoryResponse(thirdProductCategory.getNum(), thirdProductCategory.getCategoryName());
    }

    public static List<ThirdProductCategoryResponse> listOf(Set<ThirdProductCategory> thirdProductCategories) {
        return thirdProductCategories.stream()
                .map(ThirdProductCategoryResponse::of)
                .sorted(Comparator.comparing(ThirdProductCategoryResponse::getCategoryNum))
                .collect(Collectors.toList());
    }
}
