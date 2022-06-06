package com.min.bunjang.product.model;

import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.product.dto.request.ProductCreateOrUpdateRequest;
import com.min.bunjang.productinquire.model.ProductInquire;
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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_product_name_trade_location", columnList = "productName, tradeLocation"),
        @Index(name = "idx_trade_location", columnList = "tradeLocation")
})
public class Product extends BasicEntity {

    @NotBlank
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_product_category_num")
    private FirstProductCategory firstProductCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_product_category_num")
    private SecondProductCategory secondProductCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "third_product_category_num")
    private ThirdProductCategory thirdProductCategory;

    private String tradeLocation;

    //이넘 컨버터를 왜 사용해야 하는지 알게되었다면 여기에 사용해도 되는 것인지 판단후 사용할 것. 당장은 @Enumerated(EnumType.STRING)로 사용
    @Enumerated(EnumType.STRING)
    private ProductTradeState productTradeState;

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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "productNum", orphanRemoval = true)
    private Set<ProductInquire> productInquires = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<ProductPhoto> productPhotos = new ArrayList<>();


    public Product(String productName) {
        this.productName = productName;
    }

    private Product(
            String productName,
            FirstProductCategory firstProductCategory,
            SecondProductCategory secondProductCategory,
            ThirdProductCategory thirdProductCategory,
            String tradeLocation,
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
        this.tradeLocation = tradeLocation;
        this.productTradeState = ProductTradeState.SOLD_ING;
        this.productQualityState = productQualityState;
        this.exchangeState = exchangeState;
        this.productPrice = productPrice;
        this.deliveryChargeInPrice = deliveryChargeInPrice;
        this.productExplanation = productExplanation;
        this.productAmount = productAmount;
        this.store = store;
        this.hits = 0;
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
                productCreateOrUpdateRequest.getTradeLocation(),
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
        this.firstProductCategory = firstProductCategory;
        this.secondProductCategory = secondProductCategory;
        this.thirdProductCategory = thirdProductCategory;
        this.tradeLocation = productCreateOrUpdateRequest.getTradeLocation();
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

    public int getWishProductCount() {
        if (this.wishProducts.isEmpty()) {
            return 0;
        }
        return this.wishProducts.size();
    }

    public Store checkAndReturnStore() {
        if (this.store == null) {
            throw new ImpossibleException("상품이 등록된 상점이 없습니다. 잘못된 요청입니다.");
        }

        return this.store;
    }

    public void updateProductTradeState(ProductTradeState productTradeState) {
        this.productTradeState = productTradeState;
    }

}
