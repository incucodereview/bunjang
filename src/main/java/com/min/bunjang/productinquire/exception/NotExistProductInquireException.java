package com.min.bunjang.productinquire.exception;

public class NotExistProductInquireException extends RuntimeException{
    private static final String MESSAGE = "존재하지 않는 상품문의 입니다.";

    public NotExistProductInquireException() {
        super(MESSAGE);
    }
}
