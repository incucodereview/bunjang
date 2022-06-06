package com.min.bunjang.trade.model;

public enum TradeState {
    TRADE_ING("거래중"),
    TRADE_COMPLETE("거래완료"),
    TRADE_CANCEL("거래 취소");

    private String desc;

    TradeState(String desc) {
        this.desc = desc;
    }
}
