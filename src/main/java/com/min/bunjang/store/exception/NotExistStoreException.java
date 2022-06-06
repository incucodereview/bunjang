package com.min.bunjang.store.exception;

public class NotExistStoreException extends RuntimeException{
    private static final String MESSAGE = "잘못된 요청입니다. 상점이 존재하지 않습니다.";
    public NotExistStoreException() {
        super(MESSAGE);
    }
}
