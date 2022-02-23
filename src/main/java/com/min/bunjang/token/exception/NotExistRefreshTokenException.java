package com.min.bunjang.token.exception;

public class NotExistRefreshTokenException extends RuntimeException{
    private static final String MESSAGE = "리프레시토큰이 존재하지 않습니다. 잘못된 접근입니다.";
    public NotExistRefreshTokenException() {
        super(MESSAGE);
    }
}
