package com.min.bunjang.storeinquire.exception;

public class NotExistStoreInquireException extends RuntimeException {
    private static final String MESSAGE = "상점문의가 존재하지 않습니다. 잘못된 접근 입니다.";

    public NotExistStoreInquireException() {
        super(MESSAGE);
    }
}
