package com.min.bunjang.acceptance.join;

import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.acceptance.common.AcceptanceTestConfig;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.join.confirmtoken.model.ConfirmationToken;
import com.min.bunjang.join.confirmtoken.repository.ConfirmationTokenRepository;
import com.min.bunjang.join.controller.EmailJoinControllerPath;
import com.min.bunjang.join.dto.JoinRequest;
import com.min.bunjang.join.dto.TempJoinRequest;
import com.min.bunjang.member.model.JoinTempMember;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.member.repository.JoinTempMemberRepository;
import com.min.bunjang.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.stream.Stream;

public class JoinAcceptanceTest extends AcceptanceTestConfig {

    @Autowired
    private JoinTempMemberRepository joinTempMemberRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @TestFactory
    Stream<DynamicTest> dynamicTestStream() {
        String email = "urisegea@naver.com";
        String password = "password";
        String name = "name";
        String phone = "phone";
        LocalDate birthDate = LocalDate.of(2000, 10, 10);
        String token = "";

        return Stream.of(
                DynamicTest.dynamicTest("임시 회원가입 요청", () -> {
                    //given
                    TempJoinRequest tempJoinRequest = new TempJoinRequest(email, password, name, phone, birthDate);

                    //when
                    임시회원가입_요청(tempJoinRequest);

                    //then
                    임시회원가입_검증(email, password, name, phone, birthDate);
                }),

                DynamicTest.dynamicTest("이메일 인증", () -> {
                    //when
                    String confirmationToken = confirmationTokenRepository.findByEmail(email).get().getId();
                    String certifiedMemberEmail = 이메일인증_요청(confirmationToken);

                    //then
                    이메일인증_검증(email, certifiedMemberEmail);
                }),

                DynamicTest.dynamicTest("이메일 인증후 회원가입 요청", () -> {
                    //given
                    JoinRequest joinRequest = new JoinRequest(email);

                    //when
                    인증후일반회원_등록_요청(joinRequest);

                    //then
                    인증후일반회원_등록_검증(email, password, name, phone, birthDate);
                })
        );
    }

    private void 임시회원가입_요청(TempJoinRequest tempJoinRequest) {
        postApi(EmailJoinControllerPath.JOIN_TEMP_MEMBER_REQUEST, tempJoinRequest, new TypeReference<RestResponse<Void>>() {}, "");
    }

    private void 임시회원가입_검증(String email, String password, String name, String phone, LocalDate birthDate) {
        JoinTempMember joinTempMember = joinTempMemberRepository.findById(email).get();

        Assertions.assertThat(joinTempMember.getEmail()).isEqualTo(email);
        Assertions.assertThat(bCryptPasswordEncoder.matches(password, joinTempMember.getPassword())).isTrue();
        Assertions.assertThat(joinTempMember.getName()).isEqualTo(name);
        Assertions.assertThat(joinTempMember.getPhone()).isEqualTo(phone);
        Assertions.assertThat(joinTempMember.getBirthDate().getYear()).isEqualTo(birthDate.getYear());
        Assertions.assertThat(joinTempMember.getBirthDate().getMonthValue()).isEqualTo(birthDate.getMonthValue());
        Assertions.assertThat(joinTempMember.getBirthDate().getDayOfMonth()).isEqualTo(birthDate.getDayOfMonth());

        ConfirmationToken confirmationToken = confirmationTokenRepository.findByEmail(email).get();

        Assertions.assertThat(confirmationToken.isExpired()).isFalse();
    }

    private String 이메일인증_요청(String token) {
        String path = EmailJoinControllerPath.CONFIRM_EMAIL_REQUEST + "?token=" + token;
        String certifiedMemberEmail = getApi(path, "", new TypeReference<RestResponse<String>>() {}).getResult();
        return certifiedMemberEmail;
    }

    private void 이메일인증_검증(String email, String certifiedMemberEmail) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByEmail(certifiedMemberEmail).get();
        Assertions.assertThat(confirmationToken.isExpired()).isTrue();
        Assertions.assertThat(certifiedMemberEmail).isEqualTo(email);
    }

    private void 인증후일반회원_등록_요청(JoinRequest joinRequest) {
        postApi(EmailJoinControllerPath.JOIN_MEMBER_REQUEST, joinRequest, new TypeReference<RestResponse<Void>>() {}, "");
    }

    private void 인증후일반회원_등록_검증(String email, String password, String name, String phone, LocalDate birthDate) {
        Member member = memberRepository.findByEmail(email).get();
        Assertions.assertThat(member.getEmail()).isEqualTo(email);
        Assertions.assertThat(bCryptPasswordEncoder.matches(password, member.getPassword())).isTrue();
        Assertions.assertThat(member.getName()).isEqualTo(name);
        Assertions.assertThat(member.getPhone()).isEqualTo(phone);
        Assertions.assertThat(member.getBirthDate().getYear()).isEqualTo(birthDate.getYear());
        Assertions.assertThat(member.getBirthDate().getMonthValue()).isEqualTo(birthDate.getMonthValue());
        Assertions.assertThat(member.getBirthDate().getDayOfMonth()).isEqualTo(birthDate.getDayOfMonth());
        Assertions.assertThat(member.getMemberRole()).isEqualTo(MemberRole.ROLE_MEMBER);
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}
