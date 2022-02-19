package com.min.bunjang.acceptance;

import com.min.bunjang.login.controller.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("h2")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginAcceptanceTest {

    @Autowired
    private LoginService loginService;


    @Test
    void name() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    }
}
