package com.min.bunjang.member.exception;

public class NotExistTempMemberException extends RuntimeException{
    private static final String MESSAGE = "임시회원을 찾을수 없습니다.";

    public NotExistTempMemberException() {
        super(MESSAGE);
    }
}
