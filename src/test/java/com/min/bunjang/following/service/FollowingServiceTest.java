package com.min.bunjang.following.service;

import com.min.bunjang.config.ServiceTestConfig;
import com.min.bunjang.following.dto.request.FollowingCreateRequest;
import com.min.bunjang.following.model.Following;
import com.min.bunjang.following.repository.FollowingRepository;
import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.store.model.Store;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class FollowingServiceTest extends ServiceTestConfig {

    @Autowired
    private FollowingService followingService;

    @Autowired
    private FollowingRepository followingRepository;

    @DisplayName("팔로잉 정보가 등록된다.")
    @Test
    public void following_create() {
        //given
        String followerEmail = "urisegea@naver.com";
        String followerPassword = "password";
        Member followerMember = MemberAcceptanceHelper.회원가입(followerEmail, followerPassword, memberRepository, bCryptPasswordEncoder);

        String followedEmail = "writer@naver.com";
        String followedPassword = "password!writer";
        Member followedMember = MemberAcceptanceHelper.회원가입(followedEmail, followedPassword, memberRepository, bCryptPasswordEncoder);

        Store follower = StoreAcceptanceHelper.상점생성(followerMember, storeRepository);
        Store followed = StoreAcceptanceHelper.상점생성(followedMember, storeRepository);

        FollowingCreateRequest followingCreateRequest = new FollowingCreateRequest(follower.getNum(), followed.getNum());

        //when
        followingService.createFollowing(followerEmail, followingCreateRequest);

        //then
        List<Following> all = followingRepository.findAll();
        Assertions.assertThat(all).isNotNull();
        Following following = all.get(0);
        Assertions.assertThat(following.getFollowerStore()).isNotNull();
        Assertions.assertThat(following.getFollowedStore()).isNotNull();
    }
}