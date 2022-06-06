package com.min.bunjang.trade.model;

import com.min.bunjang.common.exception.ImpossibleException;
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

    public void cancelTrade() {
        this.tradeState = TradeState.TRADE_CANCEL;
    }

    public void completeTrade() {
        this.tradeState = TradeState.TRADE_COMPLETE;
    }

    public void checkMatchSellerOrBuyerByEmail(String email) {
        if (!this.seller.verifyMatchMember(email) && !this.buyer.verifyMatchMember(email)) {
            throw new ImpossibleException("판매자도 구매자도 아닌 회원의 거래 취소 요청입니다. 잘못된 접근입니다.");
        }
    }
}
