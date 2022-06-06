package com.min.bunjang.category.model;

import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FirstProductCategory extends BasicEntity {

    @NotBlank
    private String categoryName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "firstProductCategory", orphanRemoval = true)
    private Set<SecondProductCategory> secondProductCategories = new HashSet<>();

    private FirstProductCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public static FirstProductCategory createFirstProductCategory(String categoryName) {
        return new FirstProductCategory(categoryName);
    }
}
