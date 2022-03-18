package com.min.bunjang.following.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FollowingCreateResponse {
    @NotNull
    private Long followerStoreNum;
    @NotNull
    private Long followedStoreNum;
}
