package com.min.bunjang.member.dto;

import com.min.bunjang.member.model.MemberGender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberGenderUpdateRequest {
    @NotNull
    private MemberGender memberGender;
}
