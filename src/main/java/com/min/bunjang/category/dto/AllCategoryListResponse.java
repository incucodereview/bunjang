package com.min.bunjang.category.dto;

import com.min.bunjang.category.model.FirstProductCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AllCategoryListResponse {
    private List<FirstProductCategoryResponse> firstProductCategoryResponseList;
}
