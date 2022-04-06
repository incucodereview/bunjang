package com.min.bunjang.following.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.following.dto.FollowingListResponse;
import com.min.bunjang.following.service.FollowingViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class FollowingViewController {
    private final FollowingViewService followingViewService;

    @GetMapping(FollowingViewControllerPath.FOLLOWINGS_FIND_BY_STORE)
    public RestResponse<FollowingListResponse> findFollowingsByStore(
            @NotNull @PathVariable Long storeNum
    ) {
        FollowingListResponse followingListResponse = followingViewService.findThatStoreHaveFollowings(storeNum);
        return RestResponse.of(HttpStatus.OK, followingListResponse);
    }

    @GetMapping(FollowingViewControllerPath.FOLLOWERS_FIND_BY_STORE)
    public RestResponse<FollowingListResponse> findFollowersByStore(
            @NotNull @PathVariable Long storeNum
    ) {
        FollowingListResponse followerListResponse = followingViewService.findThatStoreHaveFollowers(storeNum);
        return RestResponse.of(HttpStatus.OK, followerListResponse);
    }
}
