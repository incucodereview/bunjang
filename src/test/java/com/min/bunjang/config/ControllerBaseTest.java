package com.min.bunjang.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.min.bunjang.following.dto.request.FollowingCreateRequest;
import com.min.bunjang.security.CustomPrincipalDetailsService;
import com.min.bunjang.token.jwt.JwtAccessDeniedHandler;
import com.min.bunjang.token.jwt.JwtAuthenticationEntryPoint;
import com.min.bunjang.token.jwt.TokenProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("h2")
@ExtendWith(MockitoExtension.class)
public class ControllerBaseTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected TokenProvider tokenProvider;

    @MockBean
    protected CustomPrincipalDetailsService customPrincipalDetailsService;

    @MockBean
    protected JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    protected JwtAccessDeniedHandler jwtAccessDeniedHandler;

    protected void requestExpect403(FollowingCreateRequest followingCreateRequest, MockHttpServletRequestBuilder httpMethod) throws Exception {
        mockMvc.perform(httpMethod
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, "")
                        .content(objectMapper.writeValueAsString(followingCreateRequest)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
