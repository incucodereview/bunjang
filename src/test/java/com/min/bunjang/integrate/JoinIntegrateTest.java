package com.min.bunjang.integrate;

import com.min.bunjang.config.IntegrateBaseTest;
import com.min.bunjang.join.confirmtoken.model.ConfirmationToken;
import com.min.bunjang.join.confirmtoken.repository.ConfirmationTokenRepository;
import com.min.bunjang.join.controller.EmailJoinControllerPath;
import com.min.bunjang.join.dto.JoinRequest;
import com.min.bunjang.join.dto.TempJoinRequest;
import com.min.bunjang.member.model.JoinTempMember;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.member.repository.JoinTempMemberRepository;
import com.min.bunjang.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JoinIntegrateTest extends IntegrateBaseTest {

    @Autowired
    private JoinTempMemberRepository joinTempMemberRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("임시회원가입 통합테스트")
    @Test
    public void 임시회원_가입() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        String name = "name";
        String phone = "phone";
        LocalDate birthDate = LocalDate.of(2000, 10, 10);

        TempJoinRequest tempJoinRequest = new TempJoinRequest(email, password, name, phone, birthDate);

        //when
        mockMvc.perform(post(EmailJoinControllerPath.JOIN_TEMP_MEMBER_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(tempJoinRequest)))
                .andExpect(status().isOk());

        //then
        List<JoinTempMember> tempMembers = joinTempMemberRepository.findAll();
        Assertions.assertThat(tempMembers).hasSize(1);
    }

    @DisplayName("회원가입 통합테스트")
    @Test
    public void 회원가입() throws Exception {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        String name = "name";
        String phone = "phone";
        LocalDate birthDate = LocalDate.of(2000, 10, 10);

        JoinTempMember joinTempMember = JoinTempMember.createJoinTempMember(new TempJoinRequest(email, password, name, phone, birthDate), bCryptPasswordEncoder);
        joinTempMemberRepository.save(joinTempMember);

        ConfirmationToken emailConfirmationToken = ConfirmationToken.createEmailConfirmationToken(email);
        emailConfirmationToken.useToken();
        confirmationTokenRepository.save(emailConfirmationToken);

        JoinRequest joinRequest = new JoinRequest(email, MemberGender.MEN);

        //when
        mockMvc.perform(post(EmailJoinControllerPath.JOIN_MEMBER_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(joinRequest)))
                .andExpect(status().isOk());

        //then
        List<JoinTempMember> tempMembers = joinTempMemberRepository.findAll();
        Assertions.assertThat(tempMembers).hasSize(0);

        List<Member> members = memberRepository.findAll();
        Assertions.assertThat(members).hasSize(1);
    }
}
