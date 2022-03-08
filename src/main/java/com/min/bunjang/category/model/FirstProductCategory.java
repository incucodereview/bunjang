package com.min.bunjang.category.model;

import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FirstProductCategory extends BasicEntity {

    @NotBlank
    private String categoryName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "firstProductCategory", orphanRemoval = true)
    private List<SecondProductCategory> secondProductCategories;

    private FirstProductCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public static FirstProductCategory of(String categoryName) {
        return new FirstProductCategory(categoryName);
    }
}
