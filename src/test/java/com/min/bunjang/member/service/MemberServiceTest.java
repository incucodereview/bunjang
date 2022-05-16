package com.min.bunjang.member.service;

import com.min.bunjang.common.exception.WrongRequesterException;
import com.min.bunjang.config.ServiceBaseTest;
import com.min.bunjang.helpers.MemberHelper;
import com.min.bunjang.member.dto.MemberBirthDayUpdateRequest;
import com.min.bunjang.member.dto.MemberGenderUpdateRequest;
import com.min.bunjang.member.dto.MemberPhoneUpdateRequest;
import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.security.MemberAccount;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

class MemberServiceTest extends ServiceBaseTest {

    @Autowired
    private MemberService memberService;

    @DisplayName("회원의 성별이 변경된다.")
    @Test
    public void 회원_성별_변경() {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);

        MemberGenderUpdateRequest memberGenderUpdateRequest = new MemberGenderUpdateRequest(MemberGender.WOMEN);

        MemberAccount memberAccount = new MemberAccount(member);

        //when
        memberService.changeGender(memberGenderUpdateRequest, memberAccount);

        //then
        Member updatedMember = memberRepository.findById(member.getMemberNum()).get();
        Assertions.assertThat(updatedMember.getMemberGender()).isEqualTo(memberGenderUpdateRequest.getMemberGender());
    }

    @DisplayName("[예외] 회원의 성별이 변경시 회원이 없다면 예외가 발생한다.")
    @Test
    public void 예외_회원성별변경_회원조회불가() {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);

        MemberGenderUpdateRequest memberGenderUpdateRequest = new MemberGenderUpdateRequest(MemberGender.WOMEN);

        MemberAccount memberAccount = new MemberAccount(member);

        //when & then
        Assertions.assertThatThrownBy(() -> memberService.changeGender(memberGenderUpdateRequest, null)).isInstanceOf(WrongRequesterException.class);
    }

    @DisplayName("회원의 생년월일이 변경된다.")
    @Test
    public void 회원_생년월일_변경() {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);

        MemberBirthDayUpdateRequest memberBirthDayUpdateRequest = new MemberBirthDayUpdateRequest(LocalDate.of(2021, 7, 7));
        MemberAccount memberAccount = new MemberAccount(member);

        //when
        memberService.changeBirthDay(memberBirthDayUpdateRequest, memberAccount);

        //then
        Member updatedMember = memberRepository.findById(member.getMemberNum()).get();
        Assertions.assertThat(updatedMember.getBirthDate()).isEqualTo(memberBirthDayUpdateRequest.getBirthDate());
    }

    @DisplayName("[예외] 회원의 생년월일 변경시 회원이 없다면 예외가 발생한다.")
    @Test
    public void 예외_회원생년월일변경_회원조회불가() {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);

        MemberBirthDayUpdateRequest memberBirthDayUpdateRequest = new MemberBirthDayUpdateRequest(LocalDate.of(2021, 7, 7));

        MemberAccount memberAccount = new MemberAccount(member);

        //when & then
        Assertions.assertThatThrownBy(() -> memberService.changeBirthDay(memberBirthDayUpdateRequest, null)).isInstanceOf(WrongRequesterException.class);
    }

    @DisplayName("회원의 폰넘버가 변경된다.")
    @Test
    public void 회원_폰넘버_변경() {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);

        String phone = "010-7777-7777";
        MemberPhoneUpdateRequest memberPhoneUpdateRequest = new MemberPhoneUpdateRequest(phone);
        MemberAccount memberAccount = new MemberAccount(member);

        //when
        memberService.changePhone(memberPhoneUpdateRequest, memberAccount);

        //then
        Member updatedMember = memberRepository.findById(member.getMemberNum()).get();
        Assertions.assertThat(updatedMember.getPhone()).isEqualTo(memberPhoneUpdateRequest.getPhone());
    }

    @DisplayName("[예외] 회원의 폰넘버 변경시 요청자가 없다면 예외가 발생한다.")
    @Test
    public void 예외_회원폰넘버변경_회원조회불가() {
        //given
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);

        String phone = "010-7777-7777";
        MemberPhoneUpdateRequest memberPhoneUpdateRequest = new MemberPhoneUpdateRequest(phone);

        MemberAccount memberAccount = new MemberAccount(member);

        //when & then
        Assertions.assertThatThrownBy(() -> memberService.changePhone(memberPhoneUpdateRequest, null)).isInstanceOf(WrongRequesterException.class);
    }

    @AfterEach
    void tearDown() {
        databaseFormat.clean();
    }
}