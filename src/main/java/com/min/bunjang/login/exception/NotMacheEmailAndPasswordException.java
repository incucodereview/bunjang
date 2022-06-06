package com.min.bunjang.login.exception;

public class NotMacheEmailAndPasswordException extends RuntimeException{
    private static final String MESSAGE = "아이디와 비밀번호가 일치하지 않습니다.";
    public NotMacheEmailAndPasswordException() {
        super(MESSAGE);
    }
}
