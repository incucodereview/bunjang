package com.min.bunjang.login.dto;

import com.min.bunjang.login.common.LoginValidatorMessages;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = LoginValidatorMessages.LOGIN_REQUEST_BLANK_EMAIL)
    private String email;
    @NotBlank(message = LoginValidatorMessages.LOGIN_REQUEST_BLANK_PASSWORD)
    private String password;
}
