package com.min.bunjang.product.model;

public enum ExchangeState {
    POSSIBILITY("교환가능"),
    IMPOSSIBILITY("교환불가능");

    private final String desc;

    ExchangeState(String desc) {
        this.desc = desc;
    }
}
