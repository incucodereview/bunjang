package com.min.bunjang.trade.dto.request;

import com.min.bunjang.trade.common.TradeRequestValidatorMessages;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TradeCreateRequest {
    @NotNull(message = TradeRequestValidatorMessages.TRADE_BLANK_SELLER_NUM)
    private Long sellerNum;
    @NotNull(message = TradeRequestValidatorMessages.TRADE_BLANK_BUYER_NUM)
    private Long buyerNum;
    @NotNull(message = TradeRequestValidatorMessages.TRADE_BLANK_PRODUCT_NUM)
    private Long tradeProductNum;
}
