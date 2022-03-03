package com.min.bunjang.wishproduct.model;

import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.store.model.Store;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishProduct extends BasicEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public WishProduct(Store store, Product product) {
        this.store = store;
        this.product = product;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
