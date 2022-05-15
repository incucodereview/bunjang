package com.min.bunjang.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.min.bunjang.category.repository.FirstProductCategoryRepository;
import com.min.bunjang.category.repository.SecondProductCategoryRepository;
import com.min.bunjang.category.repository.ThirdProductCategoryRepository;
import com.min.bunjang.common.database.DatabaseFormat;
import com.min.bunjang.login.service.LoginService;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.token.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@AutoConfigureMockMvc
public class IntegrateBaseTest {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected StoreRepository storeRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected FirstProductCategoryRepository firstProductCategoryRepository;

    @Autowired
    protected SecondProductCategoryRepository secondProductCategoryRepository;

    @Autowired
    protected ThirdProductCategoryRepository thirdProductCategoryRepository;

    @Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    protected DatabaseFormat databaseFormat;

    @Autowired
    protected LoginService loginService;

    protected ResultActions getRequest(String accessToken, String path) throws Exception {
        return mockMvc.perform(get(path)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, accessToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    protected ResultActions postRequest(String path, String accessToken, Object body) throws Exception {
        return mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, accessToken)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    protected ResultActions deleteRequest(String path, String accessToken, Object body) throws Exception {
        return mockMvc.perform(delete(path)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(TokenProvider.ACCESS_TOKEN_KEY_NAME, accessToken)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
