package com.min.bunjang.login.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.login.dto.LoginRequest;
import com.min.bunjang.login.exception.NotMacheEmailAndPasswordException;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.login.service.LoginService;
import com.min.bunjang.token.exception.ExpiredRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping(LoginControllerPath.LOGIN)
    public RestResponse<TokenValuesDto> login(
            @Validated @RequestBody LoginRequest loginRequest
    ) {
        TokenValuesDto tokenValuesDto = loginService.login(loginRequest);
        return RestResponse.of(HttpStatus.OK, tokenValuesDto);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotMacheEmailAndPasswordException.class)
    public RestResponse<Void> invalidTokenExceptionHandle(NotMacheEmailAndPasswordException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
