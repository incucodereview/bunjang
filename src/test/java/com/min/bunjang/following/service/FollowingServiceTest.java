package com.min.bunjang.following.service;

import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.common.exception.WrongRequesterException;
import com.min.bunjang.config.ServiceBaseTest;
import com.min.bunjang.following.dto.request.FollowingCreateRequest;
import com.min.bunjang.following.model.Following;
import com.min.bunjang.following.repository.FollowingRepository;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.StoreHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class FollowingServiceTest extends ServiceBaseTest {

    @Autowired
    private FollowingService followingService;

    @Autowired
    private FollowingRepository followingRepository;

    @DisplayName("팔로잉 정보가 등록된다.")
    @Test
    public void 팔로잉_생성() {
        //given
        String followerEmail = "urisegea@naver.com";
        String followerPassword = "password";
        Member followerMember = MemberHelper.회원가입(followerEmail, followerPassword, memberRepository, bCryptPasswordEncoder);

        String followedEmail = "writer@naver.com";
        String followedPassword = "password!writer";
        Member followedMember = MemberHelper.회원가입(followedEmail, followedPassword, memberRepository, bCryptPasswordEncoder);

        Store follower = StoreHelper.상점생성(followerMember, storeRepository);
        Store followed = StoreHelper.상점생성(followedMember, storeRepository);

        FollowingCreateRequest followingCreateRequest = new FollowingCreateRequest(follower.getNum(), followed.getNum());

        MemberAccount memberAccount = new MemberAccount(followerMember);

        //when
        followingService.createFollowing(memberAccount, followingCreateRequest);

        //then
        List<Following> all = followingRepository.findAll();
        Assertions.assertThat(all).isNotNull();
        Following following = all.get(0);
        Assertions.assertThat(following.getFollowerStore()).isNotNull();
        Assertions.assertThat(following.getFollowedStore()).isNotNull();
    }

    @DisplayName("팔로잉 정보가 삭제된다.")
    @Test
    public void 팔로잉_삭제() {
        //given
        String followerEmail = "urisegea@naver.com";
        String followerPassword = "password";
        Member followerMember = MemberHelper.회원가입(followerEmail, followerPassword, memberRepository, bCryptPasswordEncoder);

        String followedEmail = "writer@naver.com";
        String followedPassword = "password!writer";
        Member followedMember = MemberHelper.회원가입(followedEmail, followedPassword, memberRepository, bCryptPasswordEncoder);

        Store follower = StoreHelper.상점생성(followerMember, storeRepository);
        Store followed = StoreHelper.상점생성(followedMember, storeRepository);

        FollowingCreateRequest followingCreateRequest = new FollowingCreateRequest(follower.getNum(), followed.getNum());

        MemberAccount memberAccount = new MemberAccount(followerMember);

        Following following = followingRepository.save(Following.createFollowing(follower, followed));

        //when
        followingService.deleteFollowing(memberAccount, follower.getNum(), following.getNum());

        //then
        List<Following> followings = followingRepository.findAll();
        Assertions.assertThat(followings).hasSize(0);
    }

    @DisplayName("[예외] 팔로잉 생성 로직내에서 상점이 없는 경우 예외가 발생한다.")
    @Test
    public void 팔로잉_생성_상점_예외() {
        //given
        String followerEmail = "urisegea@naver.com";
        String followerPassword = "password";
        Member followerMember = MemberHelper.회원가입(followerEmail, followerPassword, memberRepository, bCryptPasswordEncoder);

        String followedEmail = "writer@naver.com";
        String followedPassword = "password!writer";
        Member followedMember = MemberHelper.회원가입(followedEmail, followedPassword, memberRepository, bCryptPasswordEncoder);

        Store follower = StoreHelper.상점생성(followerMember, storeRepository);
        Store followed = StoreHelper.상점생성(followedMember, storeRepository);

        FollowingCreateRequest createRequestNonFollowed = new FollowingCreateRequest(follower.getNum(), 3L);
        FollowingCreateRequest createRequestNonFollower = new FollowingCreateRequest(3L, followed.getNum());

        MemberAccount memberAccount = new MemberAccount(followerMember);

        //when & then
        Assertions.assertThatThrownBy(() -> followingService.createFollowing(memberAccount, createRequestNonFollowed)).isInstanceOf(NotExistStoreException.class);
        Assertions.assertThatThrownBy(() -> followingService.createFollowing(memberAccount, createRequestNonFollower)).isInstanceOf(NotExistStoreException.class);
    }

    @DisplayName("[예외] 팔로잉 생성 로직내에서 로그인이 안된 경우 예외가 발생한다.")
    @Test
    public void 팔로잉_생성_로그인_예외() {
        //given
        String followerEmail = "urisegea@naver.com";
        String followerPassword = "password";
        Member followerMember = MemberHelper.회원가입(followerEmail, followerPassword, memberRepository, bCryptPasswordEncoder);

        String followedEmail = "writer@naver.com";
        String followedPassword = "password!writer";
        Member followedMember = MemberHelper.회원가입(followedEmail, followedPassword, memberRepository, bCryptPasswordEncoder);

        Store follower = StoreHelper.상점생성(followerMember, storeRepository);
        Store followed = StoreHelper.상점생성(followedMember, storeRepository);

        FollowingCreateRequest createRequestNonFollowed = new FollowingCreateRequest(follower.getNum(), followed.getNum());

        //when & then
        Assertions.assertThatThrownBy(() -> followingService.createFollowing(null, createRequestNonFollowed)).isInstanceOf(WrongRequesterException.class);
    }

    @DisplayName("[예외] 팔로잉 생성 로직내에서 요청자 인증이 안된 경우 예외가 발생한다.")
    @Test
    public void 팔로잉_생성_요청자_인증_예외() {
        //given
        String followerEmail = "urisegea@naver.com";
        String followerPassword = "password";
        Member followerMember = MemberHelper.회원가입(followerEmail, followerPassword, memberRepository, bCryptPasswordEncoder);

        String followedEmail = "writer@naver.com";
        String followedPassword = "password!writer";
        Member followedMember = MemberHelper.회원가입(followedEmail, followedPassword, memberRepository, bCryptPasswordEncoder);

        Store follower = StoreHelper.상점생성(followerMember, storeRepository);
        Store followed = StoreHelper.상점생성(followedMember, storeRepository);

        FollowingCreateRequest createRequestNonFollowed = new FollowingCreateRequest(follower.getNum(), followed.getNum());

        MemberAccount memberAccount = new MemberAccount(followedMember);

        //when & then
        Assertions.assertThatThrownBy(() -> followingService.createFollowing(memberAccount, createRequestNonFollowed)).isInstanceOf(ImpossibleException.class);
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}