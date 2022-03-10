package com.min.bunjang.category.dto;

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
public class SecondProductCategoryResponse {
    private Long categoryNum;
    private String categoryName;
    private List<ThirdProductCategoryResponse> thirdProductCategoryResponses;

    public static SecondProductCategoryResponse of(SecondProductCategory secondProductCategory) {
        return new SecondProductCategoryResponse(
                secondProductCategory.getNum(),
                secondProductCategory.getCategoryName(),
                ThirdProductCategoryResponse.listOf(secondProductCategory.getThirdProductCategories())
        );
    }

    public static List<SecondProductCategoryResponse> listOf(Set<SecondProductCategory> secondProductCategories) {
        return secondProductCategories.stream()
                .map(SecondProductCategoryResponse::of)
                .sorted(Comparator.comparing(SecondProductCategoryResponse::getCategoryNum))
                .collect(Collectors.toList());
    }
}
