package com.min.bunjang.following.exception;

public class NotExistFollowingException extends RuntimeException{
    private static final String MESSAGE = "팔로잉이 존재하지 않습니다. 잘못된 요청입니다.";

    public NotExistFollowingException() {
        super(MESSAGE);
    }
}
