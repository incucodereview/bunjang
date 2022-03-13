package com.min.bunjang.product.model;

public enum ProductQualityState {
    NEW_PRODUCT("새상품"),
    USED_PRODUCT("중고상품");

    private final String desc;

    ProductQualityState(String desc) {
        this.desc = desc;
    }
}
