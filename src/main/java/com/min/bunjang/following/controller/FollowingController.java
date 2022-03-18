package com.min.bunjang.following.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.following.dto.FollowingCreateResponse;
import com.min.bunjang.following.service.FollowingService;
import com.min.bunjang.security.MemberAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class FollowingController {
    private final FollowingService followingService;

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PostMapping(FollowingControllerPath.FOLLOWING_CREATE)
    public RestResponse<Void> createFollowing(
            @Validated @RequestBody FollowingCreateResponse followingCreateResponse,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        followingService.createFollowing(memberAccount.getEmail(), followingCreateResponse);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @DeleteMapping (FollowingControllerPath.FOLLOWING_DELETE)
    public RestResponse<Void> deleteFollowing(
            @AuthenticationPrincipal MemberAccount memberAccount,
            @NotNull @PathVariable Long followingNum,
            @NotNull @PathVariable Long storeNum
    ) {
        followingService.deleteFollowing(memberAccount.getEmail(), followingNum, storeNum);
        return RestResponse.of(HttpStatus.OK, null);
    }
}
