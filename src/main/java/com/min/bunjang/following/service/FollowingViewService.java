package com.min.bunjang.following.service;

import com.min.bunjang.following.dto.FollowingListResponse;
import com.min.bunjang.following.dto.FollowingResponse;
import com.min.bunjang.following.model.Following;
import com.min.bunjang.following.repository.FollowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowingViewService {
    private final FollowingRepository followingRepository;

    @Transactional(readOnly = true)
    public FollowingListResponse findFollowingsByStore(Long storeNum) {
        Slice<Following> followings = followingRepository.findByFollowerStoreNum(storeNum);
        List<FollowingResponse> followingResponses = followings.getContent().stream()
                .map(following -> FollowingResponse.of(following.getNum(), following.getFollowedStore()))
                .collect(Collectors.toList());
        return new FollowingListResponse(followingResponses);
    }

    @Transactional(readOnly = true)
    public FollowingListResponse findFollowersByStore(Long storeNum) {
        Slice<Following> followers = followingRepository.findByFollowedStoreNum(storeNum);
        List<FollowingResponse> followingResponses = followers.getContent().stream()
                .map(following -> FollowingResponse.of(following.getNum(), following.getFollowerStore()))
                .collect(Collectors.toList());
        return new FollowingListResponse(followingResponses);
    }
}
