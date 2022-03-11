package com.min.bunjang.category.exception;

public class NotExistProductCategoryException extends RuntimeException{
    private static final String MESSAGE = "없는 카테고리 입니다. 잘못된 요청 입니다.";
    public NotExistProductCategoryException() {
        super(MESSAGE);
    }
}
