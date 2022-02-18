package com.min.bunjang.member.exception;

public class NotExistMemberException extends RuntimeException{
    private static final String MESSAGE = "회원을 찾을수 없습니다.";

    public NotExistMemberException() {
        super(MESSAGE);
    }
}
