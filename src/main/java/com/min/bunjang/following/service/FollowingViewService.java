package com.min.bunjang.following.service;

import com.min.bunjang.following.repository.FollowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowingViewService {
    private final FollowingRepository followingRepository;

    public void findFollowingByStore(Long storeNum) {

    }
}
