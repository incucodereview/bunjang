package com.min.bunjang.trade.model;

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
public class Trade extends BasicEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Store seller;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product tradeProduct;


}
