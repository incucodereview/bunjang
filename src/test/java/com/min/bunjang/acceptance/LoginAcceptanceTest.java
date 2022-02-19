package com.min.bunjang.acceptance;

import com.min.bunjang.login.controller.LoginControllerPath;
import com.min.bunjang.login.dto.LoginRequest;
import com.min.bunjang.login.service.LoginService;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.member.repository.MemberRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.awt.*;
import java.time.LocalDate;

@ActiveProfiles("h2")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginAcceptanceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void name() {
        //given
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String email = "email";
        String password = bCryptPasswordEncoder.encode("password");
        Member member = Member.createMember(new MemberDirectCreateDto(null,
                email,
                password,
                "min",
                "phone",
                LocalDate.of(1996, 10, 14),
                MemberRole.ROLE_ADMIN
        ));
        memberRepository.save(member);

        LoginRequest loginRequest = new LoginRequest(email, password);
        //when
        RestAssured
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .body(loginRequest)
                .when()
                    .post(LoginControllerPath.LOGIN)
                .then()
                    .statusCode(200);
        //then
    }
}
