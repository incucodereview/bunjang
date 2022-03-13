package com.min.bunjang.product.model;

public enum ProductTradeState {
    SOLD_ING("판매 중"),
    RESERVE_ING("예약 중"),
    SOLD_OUT("판매 완료");

    private String desc;

    ProductTradeState(String desc) {
        this.desc = desc;
    }
}
