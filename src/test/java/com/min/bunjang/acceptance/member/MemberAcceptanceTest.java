package com.min.bunjang.acceptance.member;

import com.fasterxml.jackson.core.type.TypeReference;
import com.min.bunjang.acceptance.common.AcceptanceTestConfig;
import com.min.bunjang.category.model.FirstProductCategory;
import com.min.bunjang.category.model.SecondProductCategory;
import com.min.bunjang.category.model.ThirdProductCategory;
import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.helpers.MemberAcceptanceHelper;
import com.min.bunjang.helpers.StoreAcceptanceHelper;
import com.min.bunjang.member.controller.MemberControllerPath;
import com.min.bunjang.member.dto.MemberBirthDayUpdateRequest;
import com.min.bunjang.member.dto.MemberGenderUpdateRequest;
import com.min.bunjang.member.dto.MemberPhoneUpdateRequest;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.token.dto.TokenValuesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDate;
import java.util.stream.Stream;

public class MemberAcceptanceTest extends AcceptanceTestConfig {

    @TestFactory
    Stream<DynamicTest> dynamicTestStream() {
        String email = "urisegea@naver.com";
        String password = "password";
        Member member = MemberAcceptanceHelper.회원가입(email, password, memberRepository, bCryptPasswordEncoder);
        TokenValuesDto loginResult = MemberAcceptanceHelper.로그인(email, password).getResult();

        return Stream.of(
                DynamicTest.dynamicTest("회원 성별 변경.", () -> {
                    //given
                    MemberGenderUpdateRequest memberGenderUpdateRequest = new MemberGenderUpdateRequest(MemberGender.WOMEN);

                    //when
                    회원_성별_변경_요청(member, loginResult, memberGenderUpdateRequest);

                    //then
                    회원_성별_변경_응답_검증(member);
                }),

                DynamicTest.dynamicTest("회원 생년월일 변경.", () -> {
                    //given
                    LocalDate birthDate = LocalDate.of(2000, 10, 10);
                    MemberBirthDayUpdateRequest memberBirthDayUpdateRequest = new MemberBirthDayUpdateRequest(birthDate);

                    //when
                    회원_생년월일_변경_요청(member, loginResult, memberBirthDayUpdateRequest);

                    //then
                    회원_생년월일_변경_응답_검증(member, birthDate);
                }),

                DynamicTest.dynamicTest("회원 폰 넘버 변경.", () -> {
                    //given
                    String phoneNum = "010-3333-8888";
                    MemberPhoneUpdateRequest memberPhoneUpdateRequest = new MemberPhoneUpdateRequest(phoneNum);

                    //when
                    회원_폰넘버_변경_요청(member, loginResult, memberPhoneUpdateRequest);

                    //then
                    회원_폰넘버_변경_응답_검증(member, phoneNum);
                })
        );
    }

    private void 회원_성별_변경_요청(Member member, TokenValuesDto loginResult, MemberGenderUpdateRequest memberGenderUpdateRequest) {
        String path = MemberControllerPath.MEMBER_CHANGE_GENDER.replace("{memberNum}", String.valueOf(member.getMemberNum()));
        patchApi(path, memberGenderUpdateRequest, new TypeReference<RestResponse<Void>>() {}, loginResult.getAccessToken());
    }

    private void 회원_성별_변경_응답_검증(Member member) {
        Member updateGenderMember = memberRepository.findById(member.getMemberNum()).get();
        Assertions.assertThat(updateGenderMember.getMemberGender()).isEqualTo(MemberGender.WOMEN);
    }

    private void 회원_생년월일_변경_요청(Member member, TokenValuesDto loginResult, MemberBirthDayUpdateRequest memberBirthDayUpdateRequest) {
        String path = MemberControllerPath.MEMBER_CHANGE_BIRTHDAY.replace("{memberNum}", String.valueOf(member.getMemberNum()));
        patchApi(path, memberBirthDayUpdateRequest, new TypeReference<RestResponse<Void>>() {}, loginResult.getAccessToken());
    }

    private void 회원_생년월일_변경_응답_검증(Member member, LocalDate birthDate) {
        Member updateBirthDateMember = memberRepository.findById(member.getMemberNum()).get();
        Assertions.assertThat(updateBirthDateMember.getBirthDate()).isEqualTo(birthDate);
    }

    private void 회원_폰넘버_변경_요청(Member member, TokenValuesDto loginResult, MemberPhoneUpdateRequest memberPhoneUpdateRequest) {
        String path = MemberControllerPath.MEMBER_CHANGE_PHONE.replace("{memberNum}", String.valueOf(member.getMemberNum()));
        patchApi(path, memberPhoneUpdateRequest, new TypeReference<RestResponse<Void>>() {}, loginResult.getAccessToken());
    }

    private void 회원_폰넘버_변경_응답_검증(Member member, String phoneNum) {
        Member updatePhoneMember = memberRepository.findById(member.getMemberNum()).get();
        Assertions.assertThat(updatePhoneMember.getPhone()).isEqualTo(phoneNum);
    }
}
