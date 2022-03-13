package com.min.bunjang.product.model;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.product.dto.ProductCreateOrUpdateRequest;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.wishproduct.model.WishProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    //    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_product_category_num")
    private FirstProductCategory firstProductCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_product_category_num")
    private SecondProductCategory secondProductCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "third_product_category_num")
    private ThirdProductCategory thirdProductCategory;

    //사진 관련 기능 s3하고 추가할것.
    private String productPhotos;

    private String exchangeLocation;

    //이넘 컨버터를 왜 사용해야 하는지 알게되었다면 여기에 사용해도 되는 것인지 판단후 사용할 것. 당장은 @Enumerated(EnumType.STRING)로 사용
    @Enumerated(EnumType.STRING)
    private ProductQualityState productQualityState;

    @Enumerated(EnumType.STRING)
    private ExchangeState exchangeState;

    @NotNull
    private int productPrice;

    @Enumerated(EnumType.STRING)
    private DeliveryChargeInPrice deliveryChargeInPrice;

    private String productExplanation;

    @NotNull
    private int productAmount;

    private int hits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_num")
    private Store store;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private Set<WishProduct> wishProducts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<ProductTag> productTags = new ArrayList<>();

    public Product(String productName) {
        this.productName = productName;
    }

    private Product(
            String productName,
            FirstProductCategory firstProductCategory,
            SecondProductCategory secondProductCategory,
            ThirdProductCategory thirdProductCategory,
            String productPhotos,
            String exchangeLocation,
            ProductQualityState productQualityState,
            ExchangeState exchangeState,
            int productPrice,
            DeliveryChargeInPrice deliveryChargeInPrice,
            String productExplanation,
            int productAmount,
            Store store
    ) {
        this.productName = productName;
        this.firstProductCategory = firstProductCategory;
        this.secondProductCategory = secondProductCategory;
        this.thirdProductCategory = thirdProductCategory;
        this.productPhotos = productPhotos;
        this.exchangeLocation = exchangeLocation;
        this.productQualityState = productQualityState;
        this.exchangeState = exchangeState;
        this.productPrice = productPrice;
        this.deliveryChargeInPrice = deliveryChargeInPrice;
        this.productExplanation = productExplanation;
        this.productAmount = productAmount;
        this.store = store;
    }

    public static Product createProduct(
            ProductCreateOrUpdateRequest productCreateOrUpdateRequest,
            FirstProductCategory firstProductCategory,
            SecondProductCategory secondProductCategory,
            ThirdProductCategory thirdProductCategory,
            Store store
    ) {
        return new Product(
                productCreateOrUpdateRequest.getProductName(),
                firstProductCategory,
                secondProductCategory,
                thirdProductCategory,
                null,
                productCreateOrUpdateRequest.getExchangeLocation(),
                productCreateOrUpdateRequest.getProductQualityState(),
                productCreateOrUpdateRequest.getExchangeState(),
                productCreateOrUpdateRequest.getProductPrice(),
                productCreateOrUpdateRequest.getDeliveryChargeInPrice(),
                productCreateOrUpdateRequest.getProductExplanation(),
                productCreateOrUpdateRequest.getProductAmount(),
                store
        );
    }

    public void productUpdate(
            ProductCreateOrUpdateRequest productCreateOrUpdateRequest,
            FirstProductCategory firstProductCategory,
            SecondProductCategory secondProductCategory,
            ThirdProductCategory thirdProductCategory
    ) {
        this.productName = productCreateOrUpdateRequest.getProductName();
        this.productPhotos = null;
        this.firstProductCategory = firstProductCategory;
        this.secondProductCategory = secondProductCategory;
        this.thirdProductCategory = thirdProductCategory;
        this.exchangeLocation = productCreateOrUpdateRequest.getExchangeLocation();
        this.productQualityState = productCreateOrUpdateRequest.getProductQualityState();
        this.exchangeState = productCreateOrUpdateRequest.getExchangeState();
        this.productPrice = productCreateOrUpdateRequest.getProductPrice();
        this.deliveryChargeInPrice = productCreateOrUpdateRequest.getDeliveryChargeInPrice();
        this.productExplanation = productCreateOrUpdateRequest.getProductExplanation();
        this.productAmount = productCreateOrUpdateRequest.getProductAmount();

    }

    public void updateProductTag(List<ProductTag> productTags) {
        productTags.clear();
        for (ProductTag productTag : productTags) {
            productTag.defineRelationToProduct(this);
        }
        this.productTags.addAll(productTags);
    }

    public void addHitsCount(String email) {
        if (email == null) {
            return;
        }

        this.hits += 1;
    }

}
