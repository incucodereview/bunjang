package com.min.bunjang.following.repository;

import com.min.bunjang.following.model.Following;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Following, Long> {
    //TODO 명확한 네이밍으로 변경할 필요성이 느껴짐 너무 햇갈린다;;;;
    Slice<Following> findByFollowerStoreNum(Long followerStoreNum);
    Slice<Following> findByFollowedStoreNum(Long followedStoreNum);
}
