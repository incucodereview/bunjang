package com.min.bunjang.join.dto;

import com.min.bunjang.join.common.JoinRequestValidatorMessages;
import com.min.bunjang.member.model.MemberGender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class JoinRequest {
    @NotBlank(message = JoinRequestValidatorMessages.JOIN_BLANK_EMAIL)
    private String email;
    @NotNull(message = JoinRequestValidatorMessages.JOIN_BLANK_GENDER)
    private MemberGender memberGender;
}
