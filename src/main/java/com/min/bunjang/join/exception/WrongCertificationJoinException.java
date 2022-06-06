package com.min.bunjang.join.exception;

public class WrongCertificationJoinException extends RuntimeException {
    private static final String MESSAGE = "해당 계정은 이메일 인증이 되지 않았습니다. 다시 시도해주세요.";

    public WrongCertificationJoinException() {
        super(MESSAGE);
    }
}
