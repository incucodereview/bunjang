package com.min.bunjang.common.exception;

public class WrongRequesterException extends RuntimeException {
    public WrongRequesterException(String message) {
        super(message);
    }
}
