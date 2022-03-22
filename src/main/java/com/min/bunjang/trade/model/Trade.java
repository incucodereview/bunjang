package com.min.bunjang.trade.model;

import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.store.model.Store;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trade extends BasicEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Store seller;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product tradeProduct;

    @Enumerated(EnumType.STRING)
    private TradeState tradeState;

    private Trade(Store seller, Store buyer, Product tradeProduct, TradeState tradeState) {
        this.seller = seller;
        this.buyer = buyer;
        this.tradeProduct = tradeProduct;
        this.tradeState = tradeState;
    }

    public static Trade createTrade(Store seller, Store buyer, Product tradeProduct, TradeState tradeState) {
        return new Trade(seller, buyer, tradeProduct, tradeState);
    }
}
