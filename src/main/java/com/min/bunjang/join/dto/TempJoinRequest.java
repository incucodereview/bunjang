package com.min.bunjang.join.dto;

import com.min.bunjang.join.common.JoinRequestValidatorMessages;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TempJoinRequest {
    @NotBlank(message = JoinRequestValidatorMessages.JOIN_BLANK_EMAIL)
    private String email;
    @NotBlank(message = JoinRequestValidatorMessages.JOIN_BLANK_PASSWORD)
    private String password;
    @NotBlank(message = JoinRequestValidatorMessages.JOIN_BLANK_NAME)
    private String name;
    @NotBlank(message = JoinRequestValidatorMessages.JOIN_BLANK_PHONE)
    private String phone;
    @NotNull(message = JoinRequestValidatorMessages.JOIN_BLANK_BIRTHDATE)
    private LocalDate birthDate;
}
