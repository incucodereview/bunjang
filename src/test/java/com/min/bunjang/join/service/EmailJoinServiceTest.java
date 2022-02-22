package com.min.bunjang.join.service;

import com.min.bunjang.common.database.DatabaseCleanup;
import com.min.bunjang.join.confirmtoken.exception.WrongConfirmEmailToken;
import com.min.bunjang.join.confirmtoken.model.ConfirmationToken;
import com.min.bunjang.join.confirmtoken.repository.ConfirmationTokenRepository;
import com.min.bunjang.join.dto.EmailJoinRequest;
import com.min.bunjang.member.model.JoinTempMember;
import com.min.bunjang.member.repository.JoinTempMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;


@SpringBootTest
@ActiveProfiles("h2")
class EmailJoinServiceTest {
    @Autowired
    private JoinTempMemberRepository joinTempMemberRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailJoinService emailJoinService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @MockBean
    private JavaMailSender javaMailSender;

    @DisplayName("이메일 회원가입 - 이메일 인증전 임시회원이 생성된다")
    @Test
    void join_joinTempMember_create() {
        //given
        String email = "urisegea@naver.com";
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
        Assertions.assertThat(joinTempMember.getBirthDate().getYear()).isEqualTo(birthDate.getYear());
        Assertions.assertThat(joinTempMember.getBirthDate().getMonthValue()).isEqualTo(birthDate.getMonthValue());
        Assertions.assertThat(joinTempMember.getBirthDate().getDayOfMonth()).isEqualTo(birthDate.getDayOfMonth());

        ArgumentCaptor<SimpleMailMessage> argumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, atLeastOnce()).send(argumentCaptor.capture());
    }

    @DisplayName("이메일 인증이 완료되면 ConfirmToken 의 expired 필드가 true 가 된다.")
    @Test
    void verifyConfirmToken_expired_True() {
        //given
        String email = "email";
        ConfirmationToken confirmationToken = confirmationTokenRepository.save(ConfirmationToken.createEmailConfirmationToken(email));
        String token = confirmationToken.getId();

        //when
        emailJoinService.verifyConfirmEmailToken(token);

        //then
        ConfirmationToken updatedConfirmationToken = confirmationTokenRepository.findById(token).get();
        Assertions.assertThat(updatedConfirmationToken.isExpired()).isTrue();
    }

    @DisplayName("[예외] 잘못된 토큰값이 요청되면 WrongConfirmEmailToken 예외를 응답한")
    @Test
    void verifyConfirmToken_WrongTokenValue() {
        //given
        String email = "email";
        String wrongToken = "token";
        ConfirmationToken confirmationToken = confirmationTokenRepository.save(ConfirmationToken.createEmailConfirmationToken(email));

        //when & then
        Assertions.assertThatThrownBy(() -> emailJoinService.verifyConfirmEmailToken(wrongToken)).isInstanceOf(WrongConfirmEmailToken.class);
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}