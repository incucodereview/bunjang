package com.min.bunjang.following.repository;

import com.min.bunjang.config.ServiceBaseTest;
import com.min.bunjang.following.model.Following;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.StoreHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.store.model.Store;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import static org.junit.jupiter.api.Assertions.*;


class FollowingViewRepositoryTest extends ServiceBaseTest {

    @Autowired
    private FollowingViewRepository followingViewRepository;

    @Autowired
    private FollowingRepository followingRepository;

    @DisplayName("특정상점의 팔로워를 검색할 수 있다.")
    @Test
    public void 상점_팔로워_검색() {
        //given
        Member member = MemberHelper.회원가입("email", "pwd", memberRepository, bCryptPasswordEncoder);
        Store store = StoreHelper.상점생성(member, storeRepository);
        Member followerMember1 = MemberHelper.회원가입("follower1", "pwd", memberRepository, bCryptPasswordEncoder);
        Store followerStore1 = StoreHelper.상점생성(followerMember1, storeRepository);
        Member followerMember2 = MemberHelper.회원가입("follower2", "pwd", memberRepository, bCryptPasswordEncoder);
        Store followerStore2 = StoreHelper.상점생성(followerMember2, storeRepository);
        Following following = followingRepository.save(Following.createFollowing(followerStore1, store));
        Following following2 = followingRepository.save(Following.createFollowing(followerStore2, store));
        followingRepository.save(Following.createFollowing(followerStore1, followerStore2));

        PageRequest pageRequest = PageRequest.of(0, 30);
        //when
        Slice<Following> followedStores = followingViewRepository.findByFollowedStoreNum(store.getNum(), pageRequest);

        //then
        Assertions.assertThat(followedStores).hasSize(2);
        Assertions.assertThat(followedStores.getContent().get(0).getNum()).isEqualTo(following2.getNum());
        Assertions.assertThat(followedStores.getContent().get(1).getNum()).isEqualTo(following.getNum());
    }
}