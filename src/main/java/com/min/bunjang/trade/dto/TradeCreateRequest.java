package com.min.bunjang.trade.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TradeCreateRequest {
    @NotNull
    private Long sellerNum;
    @NotNull
    private Long buyerNum;
    @NotNull
    private Long tradeProductNum;
}
