package com.min.bunjang.join.service;

import com.min.bunjang.member.dto.EmailJoinRequest;
import com.min.bunjang.member.model.JoinTempMember;
import com.min.bunjang.member.repository.JoinTempMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;


@SpringBootTest
@ActiveProfiles("h2")
class EmailJoinServiceTest {
    @Autowired
    private EmailJoinService emailJoinService;

    @Autowired
    private JoinTempMemberRepository joinTempMemberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("이메일 회원가입 - 이메일 인증전 임시회원이 생성된다")
    @Test
    void join_joinTempMember_create() {
        //given
        String email = "email";
        String password = "password";
        String name = "name";
        String phone = "phone";
        LocalDate birthDate = LocalDate.of(2000, 10, 10);

        EmailJoinRequest emailJoinRequest = new EmailJoinRequest(
                email,
                password,
                name,
                phone,
                birthDate
        );
        //when
        emailJoinService.joinTempMember(emailJoinRequest);
        //then
        JoinTempMember joinTempMember = joinTempMemberRepository.findById(email).get();

        Assertions.assertThat(joinTempMember.getEmail()).isEqualTo(email);
        Assertions.assertThat(bCryptPasswordEncoder.matches(password, joinTempMember.getPassword())).isTrue();
        Assertions.assertThat(joinTempMember.getName()).isEqualTo(name);
        Assertions.assertThat(joinTempMember.getPhone()).isEqualTo(phone);
        Assertions.assertThat(joinTempMember.getJoinDate()).isEqualTo(birthDate);
    }
}