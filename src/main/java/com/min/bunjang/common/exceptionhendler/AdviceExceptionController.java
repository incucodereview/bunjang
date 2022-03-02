package com.min.bunjang.common.exceptionhendler;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.common.exception.ImpossibleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class AdviceExceptionController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public RestResponse<Void> Exception(Exception e) {
        log.error(e.getClass() + ": " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        return RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public RestResponse<Void> nullPointerException(NullPointerException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        return RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 입니다. 원인 : " + Arrays.toString(e.getStackTrace()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ImpossibleException.class})
    public RestResponse<Void> impossibleException(Exception e) {
        log.error(e.getClass() + ": " + e.getMessage());
        return RestResponse.error(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResponse<Void> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getBindingResult().getFieldError().getDefaultMessage());
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ExpectedException.class)
//    public RestResponse<Void> expectedException(ExpectedException e) {
//        return RestResponse.error(e.getMessage());
//    }

//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    @ExceptionHandler({CustomAccessDeniedException.class, AccessDeniedException.class})
//    public RestResponse<Void> expectedException(Exception e) {
//        return RestResponse.error("403", e.getMessage());
//    }
}
