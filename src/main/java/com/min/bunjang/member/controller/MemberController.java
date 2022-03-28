package com.min.bunjang.member.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.member.service.MemberService;
import com.min.bunjang.security.MemberAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
            @RequestParam MemberGender memberGender,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        memberService.changeGender(memberNum, memberGender, memberAccount);
        return RestResponse.of(HttpStatus.OK, null);
    }
}
