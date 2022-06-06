package com.min.bunjang.wishproduct.exception;

import javax.validation.constraints.NotNull;

public class NotExistWishProductException extends RuntimeException{
    private static final String MESSAGE = "존재하지 않는 찜상품 입니다.";

    public NotExistWishProductException() {
        super(MESSAGE);
    }
}
