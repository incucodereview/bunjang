package com.min.bunjang.following.service;

import com.min.bunjang.common.validator.MemberAndStoreValidator;
import com.min.bunjang.following.dto.request.FollowingCreateRequest;
import com.min.bunjang.following.exception.NotExistFollowingException;
import com.min.bunjang.following.model.Following;
import com.min.bunjang.following.repository.FollowingRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowingService {
    private final FollowingRepository followingRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createFollowing(String requesterEmail, FollowingCreateRequest followingCreateRequest) {
        Store followerStore = storeRepository.findById(followingCreateRequest.getFollowerStoreNum()).orElseThrow(NotExistStoreException::new);
        Store followedStore = storeRepository.findById(followingCreateRequest.getFollowedStoreNum()).orElseThrow(NotExistStoreException::new);
        MemberAndStoreValidator.verifyMemberAndStoreMatchByEmail(requesterEmail, followerStore);

        followingRepository.save(Following.createFollowing(followerStore, followedStore));
    }

    @Transactional
    public void deleteFollowing(String requesterEmail, Long storeNum, Long followingNum) {
        Store follower = storeRepository.findById(storeNum).orElseThrow(NotExistStoreException::new);
        MemberAndStoreValidator.verifyMemberAndStoreMatchByEmail(requesterEmail, follower);

        Following following = followingRepository.findById(followingNum).orElseThrow(NotExistFollowingException::new);
        followingRepository.delete(following);
    }
}
