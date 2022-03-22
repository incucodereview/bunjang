package com.min.bunjang.trade.exception;

public class NotExistTradeException extends RuntimeException{
    private static final String MESSAGE = "거래내역이 존재하지 않습니다. 잘못된 요청입니다.";

    public NotExistTradeException() {
        super(MESSAGE);
    }
}
