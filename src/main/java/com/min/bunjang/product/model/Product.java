package com.min.bunjang.product.model;

import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BasicEntity {

    @NotBlank
    private String productName;

    public Product(String productName) {
        this.productName = productName;
    }
}
