package com.min.bunjang.config;

import com.min.bunjang.common.database.DatabaseCleanup;
import com.min.bunjang.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("h2")
public class ServiceTestConfig {

    @Autowired
    protected DatabaseCleanup databaseCleanup;

    @Autowired
    protected MemberRepository memberRepository;
}
