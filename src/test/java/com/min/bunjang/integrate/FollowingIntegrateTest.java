package com.min.bunjang.integrate;

import com.min.bunjang.config.IntegrateBaseTest;
import com.min.bunjang.following.controller.FollowingControllerPath;
import com.min.bunjang.following.dto.request.FollowingCreateRequest;
import com.min.bunjang.following.model.Following;
import com.min.bunjang.following.repository.FollowingRepository;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.helpers.StoreHelper;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.token.jwt.TokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FollowingIntegrateTest extends IntegrateBaseTest {

    @Autowired
    private FollowingRepository followingRepository;

    @DisplayName("팔로잉 생성 통합테스트")
    @Test
    public void 팔로잉_생성() throws Exception {
        //given
        String followerEmail = "urisegea@naver.com";
        String followerPassword = "password";
        Member followerMember = MemberHelper.회원가입(followerEmail, followerPassword, memberRepository, bCryptPasswordEncoder);

        String followedEmail = "writer@naver.com";
        String followedPassword = "password!writer";
        Member followedMember = MemberHelper.회원가입(followedEmail, followedPassword, memberRepository, bCryptPasswordEncoder);

        Store follower = StoreHelper.상점생성(followerMember, storeRepository);
        Store followed = StoreHelper.상점생성(followedMember, storeRepository);

        TokenValuesDto tokenValuesDto = MemberHelper.로그인(followerEmail, followerPassword, loginService);

        FollowingCreateRequest followingCreateRequest = new FollowingCreateRequest(follower.getNum(), followed.getNum());

        //when
        mockMvc.perform(post(FollowingControllerPath.FOLLOWING_CREATE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, tokenValuesDto.getAccessToken())
                        .content(objectMapper.writeValueAsString(followingCreateRequest)))
                .andExpect(status().isOk())
                .andDo(print());
        //then
        List<Following> followings = followingRepository.findAll();
        Assertions.assertThat(followings).hasSize(1);
    }

    @DisplayName("팔로잉 삭제 통합테스트")
    @Test
    public void 팔로잉_삭제() throws Exception {
        //given

        String followerEmail = "urisegea@naver.com";
        String followerPassword = "password";
        Member followerMember = MemberHelper.회원가입(followerEmail, followerPassword, memberRepository, bCryptPasswordEncoder);

        String followedEmail = "writer@naver.com";
        String followedPassword = "password!writer";
        Member followedMember = MemberHelper.회원가입(followedEmail, followedPassword, memberRepository, bCryptPasswordEncoder);

        Store follower = StoreHelper.상점생성(followerMember, storeRepository);
        Store followed = StoreHelper.상점생성(followedMember, storeRepository);

        TokenValuesDto tokenValuesDto = MemberHelper.로그인(followerEmail, followerPassword, loginService);

        Following following = followingRepository.save(Following.createFollowing(follower, followed));

        //when
        String path = FollowingControllerPath.FOLLOWING_DELETE
                .replace("{storeNum}", String.valueOf(follower.getNum()))
                .replace("{followingNum}", String.valueOf(following.getNum()));

        mockMvc.perform(delete(path)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, tokenValuesDto.getAccessToken()))
                .andExpectAll(status().isOk())
                .andDo(print());
        //then
        List<Following> followings = followingRepository.findAll();
        Assertions.assertThat(followings).hasSize(0);
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}
