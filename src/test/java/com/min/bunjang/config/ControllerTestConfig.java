package com.min.bunjang.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.min.bunjang.security.CustomPrincipalDetailsService;
import com.min.bunjang.token.jwt.JwtAccessDeniedHandler;
import com.min.bunjang.token.jwt.JwtAuthenticationEntryPoint;
import com.min.bunjang.token.jwt.TokenProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("h2")
@ExtendWith(MockitoExtension.class)
public class ControllerTestConfig {

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

}
