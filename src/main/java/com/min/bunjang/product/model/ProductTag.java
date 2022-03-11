package com.min.bunjang.product.model;

import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductTag extends BasicEntity {

    private String tag;

    private Long productNum;

    public ProductTag(String tag, Long productNum) {
        this.tag = tag;
        this.productNum = productNum;
    }

    public static ProductTag createProductTag(String tag, Long productNum) {
        return new ProductTag(tag, productNum);
    }
}
