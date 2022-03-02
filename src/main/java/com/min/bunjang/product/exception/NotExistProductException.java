package com.min.bunjang.product.exception;

public class NotExistProductException extends RuntimeException{
    private static final String MESSAGE = "존재하지 않는 상품입니다. 잘못된 접근입니다.";

    public NotExistProductException() {
        super(MESSAGE);
    }
}
