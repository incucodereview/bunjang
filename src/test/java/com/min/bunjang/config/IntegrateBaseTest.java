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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
}
