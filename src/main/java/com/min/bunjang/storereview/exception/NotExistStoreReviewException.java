package com.min.bunjang.storereview.exception;

public class NotExistStoreReviewException extends RuntimeException{
    private static final String MESSAGE = "존재하지 않는 상점후기 입니다. 잘못된 접근입니다.";

    public NotExistStoreReviewException() {
        super(MESSAGE);
    }
}
