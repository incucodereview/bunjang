package com.min.bunjang.join.dto;

import com.min.bunjang.member.model.MemberGender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class JoinRequest {
    @NotBlank
    private String email;
    private MemberGender memberGender;
}
