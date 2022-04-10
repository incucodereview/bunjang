package com.min.bunjang.following.dto.request;

import com.min.bunjang.following.common.FollowingRequestValidatorMessages;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FollowingCreateRequest {
    @NotNull(message = FollowingRequestValidatorMessages.FOLLOWING_BLANK_FOLLOWER)
    private Long followerStoreNum;
    @NotNull(message = FollowingRequestValidatorMessages.FOLLOWING_BLANK_FOLLOWED)
    private Long followedStoreNum;
}
