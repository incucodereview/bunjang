package com.min.bunjang.following.repository;

import com.min.bunjang.following.model.Following;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Following, Long> {
    Slice<Following> findByFollowerStoreNum(Long followerStoreNUm);
}
