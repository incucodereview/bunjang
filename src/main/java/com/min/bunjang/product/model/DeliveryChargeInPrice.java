package com.min.bunjang.product.model;

public enum DeliveryChargeInPrice {
    INCLUDED("배송피 포함"),
    EXCLUDED("배송비 별도");

    private final String desc;

    DeliveryChargeInPrice(String desc) {
        this.desc = desc;
    }
}
