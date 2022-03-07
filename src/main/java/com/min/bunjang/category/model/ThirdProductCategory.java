package com.min.bunjang.category.model;

import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThirdProductCategory extends BasicEntity {

    @NotBlank
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "num")
    private SecondProductCategory secondProductCategory;

    private ThirdProductCategory(String categoryName, SecondProductCategory secondProductCategory) {
        this.categoryName = categoryName;
        this.secondProductCategory = secondProductCategory;
    }

    public static ThirdProductCategory createThirdCategory(String categoryName, SecondProductCategory secondProductCategory) {
        return new ThirdProductCategory(categoryName, secondProductCategory);
    }
}
