package com.min.bunjang.join.confirmtoken.exception;

public class WrongConfirmEmailToken extends RuntimeException{
    private static final String MESSAGE = "없는 토큰값입니다. 인증 기간이 만료되었거나 잘못된 접근입니다.";
    public WrongConfirmEmailToken() {
        super(MESSAGE);
    }
}
