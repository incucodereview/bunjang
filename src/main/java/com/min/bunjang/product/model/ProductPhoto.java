package com.min.bunjang.product.model;

import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.lang.invoke.LambdaConversionException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductPhoto extends BasicEntity {

    @NotNull
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private ProductPhoto(String filePath, Product product) {
        this.filePath = filePath;
        this.product = product;
    }

    public static ProductPhoto of(String filePath, Product product) {
        return new ProductPhoto(filePath, product);
    }
}
