package com.min.bunjang.product.model;

import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.wishproduct.model.WishProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BasicEntity {

    @NotBlank
    private String productName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private Set<WishProduct> wishProducts = new HashSet<>();

    public Product(String productName) {
        this.productName = productName;
    }

    //TODO 사용여부가 있을지 구현 단계에선 파악이 힘들다. 사용하며 더 자주 사용되는 쪽으로 옮겨도 되고 삭제해도 무방.
    public void addWishProduct(WishProduct wishProduct) {
        this.wishProducts.add(wishProduct);
        wishProduct.setProduct(this);
    }
}
