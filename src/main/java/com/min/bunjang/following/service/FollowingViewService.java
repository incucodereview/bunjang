package com.min.bunjang.following.service;

import com.min.bunjang.following.dto.response.FollowingListResponse;
import com.min.bunjang.following.dto.response.FollowingResponse;
import com.min.bunjang.following.model.Following;
import com.min.bunjang.following.repository.FollowingRepository;
import com.min.bunjang.following.repository.FollowingViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowingViewService {
    private final FollowingRepository followingRepository;
    private final FollowingViewRepository followingViewRepository;

    //TODO 얘도 쿼리 개선 해야함
    @Transactional(readOnly = true)
    public FollowingListResponse findFollowingsOfStore(Long storeNum) {
        Slice<Following> followings = followingRepository.findByFollowerStoreNum(storeNum);
        List<FollowingResponse> followingResponses = followings.getContent().stream()
                .map(following -> FollowingResponse.of(following.getNum(), following.getFollowedStore()))
                .collect(Collectors.toList());
        return new FollowingListResponse(followingResponses);
    }

    @Transactional(readOnly = true)
    public FollowingListResponse findFollowersOfStore(Long storeNum, Pageable pageable) {
        Slice<Following> followers = followingViewRepository.findByFollowedStoreNum(storeNum, pageable);
        FollowingResponse.of(followers.getContent().get(0).getNum(), followers.getContent().get(0).getFollowedStore());
        List<FollowingResponse> followingResponses = followers.getContent().stream()
                .map(following -> FollowingResponse.of(following.getNum(), following.getFollowerStore()))
                .collect(Collectors.toList());
        return new FollowingListResponse(followingResponses);
    }
}
