package com.min.bunjang.product.model;

import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductTag extends BasicEntity {

    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public ProductTag(String tag, Product product) {
        this.tag = tag;
        this.product = product;
    }

    public static ProductTag createProductTag(String tag, Product product) {
        return new ProductTag(tag, product);
    }

    public void defineRelationToProduct(Product product) {
        this.product = product;
        product.addTag(this);
    }
}
