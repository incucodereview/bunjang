package com.min.bunjang.following.service;

import com.min.bunjang.following.model.Following;
import com.min.bunjang.following.repository.FollowingRepository;
import com.min.bunjang.store.dto.StoreSimpleResponse;
import com.min.bunjang.store.dto.StoreSimpleResponses;
import com.min.bunjang.store.model.Store;
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
    public StoreSimpleResponses findFollowingByStore(Long storeNum) {
        Slice<Following> followings = followingRepository.findByFollowerStoreNum(storeNum);
        List<Store> followedStores = followings.getContent().stream()
                .map(Following::getFollowedStore)
                .collect(Collectors.toList());
        return new StoreSimpleResponses(StoreSimpleResponse.listOf(followedStores));
    }
}
