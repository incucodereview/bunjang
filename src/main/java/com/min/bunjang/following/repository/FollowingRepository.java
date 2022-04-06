package com.min.bunjang.following.repository;

import com.min.bunjang.following.model.Following;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Following, Long> {
    //storeNum의 팔로워 리스트
    Slice<Following> findByFollowerStoreNum(Long followerStoreNum);
    //storeNum의 팔로잉 리스트
    Slice<Following> findByFollowedStoreNum(Long followedStoreNum);
}
