package com.min.bunjang.member.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.member.dto.MemberBirthDayUpdateRequest;
import com.min.bunjang.member.dto.MemberGenderUpdateRequest;
import com.min.bunjang.member.dto.MemberPhoneUpdateRequest;
import com.min.bunjang.member.service.MemberService;
import com.min.bunjang.security.MemberAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PatchMapping(MemberControllerPath.MEMBER_CHANGE_GENDER)
    public RestResponse<Void> changeGender(
            @NotNull @PathVariable Long memberNum,
            @Validated @RequestBody MemberGenderUpdateRequest memberGenderUpdateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        memberService.changeGender(memberNum, memberGenderUpdateRequest, memberAccount);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PatchMapping(MemberControllerPath.MEMBER_CHANGE_BIRTHDAY)
    public RestResponse<Void> changeBirthDay(
            @NotNull @PathVariable Long memberNum,
            @Validated @RequestBody MemberBirthDayUpdateRequest memberBirthDayUpdateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        memberService.changeBirthDay(memberNum, memberBirthDayUpdateRequest, memberAccount);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PatchMapping(MemberControllerPath.MEMBER_CHANGE_PHONE)
    public RestResponse<Void> changePhone(
            @NotNull @PathVariable Long memberNum,
            @Validated @RequestBody MemberPhoneUpdateRequest memberPhoneUpdateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        memberService.changePhone(memberNum, memberPhoneUpdateRequest, memberAccount);
        return RestResponse.of(HttpStatus.OK, null);
    }
}
