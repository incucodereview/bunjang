package com.min.bunjang.following.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.following.dto.FollowingCreateResponse;
import com.min.bunjang.following.service.FollowingService;
import com.min.bunjang.security.MemberAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FollowingController {
    private final FollowingService followingService;

    @PostMapping(FollowingControllerPath.FOLLOWING_CREATE)
    public RestResponse<Void> createFollowing(
            @Validated @RequestBody FollowingCreateResponse followingCreateResponse,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        followingService.createFollowing(memberAccount.getEmail(), followingCreateResponse);
        return RestResponse.of(HttpStatus.OK, null);
    }
}
