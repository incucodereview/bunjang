package com.min.bunjang.category.model;

import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecondProductCategory extends BasicEntity {

    @NotBlank
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "num")
    private FirstProductCategory firstProductCategory;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "secondProductCategory", orphanRemoval = true)
    private List<ThirdProductCategory> thirdProductCategories;

    private SecondProductCategory(String categoryName, FirstProductCategory firstProductCategory) {
        this.categoryName = categoryName;
        this.firstProductCategory = firstProductCategory;
    }

    public static SecondProductCategory createSecondCategory(String categoryName, FirstProductCategory firstProductCategory) {
        return new SecondProductCategory(categoryName, firstProductCategory);
    }
}
