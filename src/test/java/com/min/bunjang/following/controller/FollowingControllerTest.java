package com.min.bunjang.following.controller;

import com.min.bunjang.config.ControllerTestConfig;
import com.min.bunjang.following.dto.request.FollowingCreateRequest;
import com.min.bunjang.following.service.FollowingService;
import com.min.bunjang.login.controller.LoginController;
import com.min.bunjang.token.jwt.TokenProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FollowingController.class)
class FollowingControllerTest extends ControllerTestConfig {

    @MockBean
    private FollowingService followingService;

    @Disabled
    @DisplayName("로그인 없이 접근한 경우 ")
    @Test
    public void following_403_exception() throws Exception {
        //given
        FollowingCreateRequest followingCreateRequest = new FollowingCreateRequest(1L, 2L);

        //when && then
        mockMvc.perform(post(FollowingControllerPath.FOLLOWING_CREATE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TokenProvider.ACCESS_TOKEN_KEY_OF_HEADER, "")
                .content(objectMapper.writeValueAsString(followingCreateRequest)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

//    @DisplayName("로그인은 했으나 권한이 없는 경우 ")
//    @Test
//    public void following_401_exception() throws Exception {
//        //given
//        FollowingCreateRequest followingCreateRequest = new FollowingCreateRequest(1L, 2L);
//        new TokenProvider()
//
//
//        //when && then
//        mockMvc.perform(post(FollowingControllerPath.FOLLOWING_CREATE)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .header(TokenProvider.ACCESS_TOKEN_KEY_OF_HEADER, "")
//                .content(objectMapper.writeValueAsString(followingCreateRequest)))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }

}