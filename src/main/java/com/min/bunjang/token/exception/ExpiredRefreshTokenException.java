package com.min.bunjang.token.exception;

public class ExpiredRefreshTokenException extends RuntimeException{
    private static final String MESSAGE = "리프레시 토큰의 기한이 만료되었습니다. 다시 로그인 해주세요";
    public ExpiredRefreshTokenException() {
        super(MESSAGE);
    }
}
