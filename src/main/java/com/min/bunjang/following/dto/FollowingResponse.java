package com.min.bunjang.following.dto;

import com.min.bunjang.store.dto.StoreSimpleResponse;
import com.min.bunjang.store.model.Store;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FollowingResponse {
    private Long followingNum;
    private StoreSimpleResponse storeSimpleResponses;

    public static FollowingResponse of(Long followingNum, Store store) {
        return new FollowingResponse(followingNum, StoreSimpleResponse.of(store));
    }

}
