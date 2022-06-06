package com.min.bunjang.config;

import com.min.bunjang.common.database.DatabaseFormat;
import com.min.bunjang.login.service.LoginService;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("h2")
public class ServiceBaseTest {

    @Autowired
    protected DatabaseFormat databaseFormat;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    protected StoreRepository storeRepository;

    @Autowired
    protected LoginService loginService;

}
