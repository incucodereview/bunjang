package com.min.bunjang.category.model;

import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FirstProductCategory extends BasicEntity {

    @NotBlank
    private String categoryName;

    private FirstProductCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public static FirstProductCategory of(String categoryName) {
        return new FirstProductCategory(categoryName);
    }
}
