package com.min.bunjang.product.model;

import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.wishproduct.model.WishProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    //사진 관련 기능 s3하고 추가할것.

    //카테고리 구현

    private String exchangeLocation;

    //이넘 컨버터를 왜 사용해야 하는지 알게되었다면 여기에 사용해도 되는 것인지 판단후 사용할 것. 당장은 @Enumerated(EnumType.STRING)로 사용
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @Enumerated(EnumType.STRING)
    private ExchangeState exchangeState;

    @NotNull
    private int productPrice;

    private String productExplanation;

    @NotNull
    private int productAmount;

//    @NotNull
    private Long firstProductCategoryNum;
    private Long secondProductCategoryNum;
    private Long thirdProductCategoryNum;

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
