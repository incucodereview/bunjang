package com.min.bunjang.config;

import com.min.bunjang.common.database.DatabaseCleanup;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.product.model.ProductTag;
import com.min.bunjang.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("h2")
public class ServiceTestConfig {

    @Autowired
    protected DatabaseCleanup databaseCleanup;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    protected StoreRepository storeRepository;
}
