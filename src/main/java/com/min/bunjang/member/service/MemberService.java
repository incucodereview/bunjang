package com.min.bunjang.member.service;

import com.min.bunjang.common.validator.RightRequesterChecker;
import com.min.bunjang.member.dto.MemberBirthDayUpdateRequest;
import com.min.bunjang.member.dto.MemberGenderUpdateRequest;
import com.min.bunjang.member.dto.MemberPhoneUpdateRequest;
import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.security.MemberAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //TODO 회원검증이 애매함 이건 무조건 토큰값이 올바른 값이라고 가정하는 코드임.. 요청DTO에 비교할 값을 더 주는것도 괜찮아 보임.
    @Transactional
    public void changeGender(MemberGenderUpdateRequest memberGenderUpdateRequest, MemberAccount memberAccount) {
        RightRequesterChecker.verifyLoginRequest(memberAccount);
        Member member = memberRepository.findByEmail(memberAccount.getEmail()).orElseThrow(NotExistMemberException::new);
        member.changeGender(memberGenderUpdateRequest.getMemberGender());
    }

    @Transactional
    public void changeBirthDay(MemberBirthDayUpdateRequest memberBirthDayUpdateRequest, MemberAccount memberAccount) {
        RightRequesterChecker.verifyLoginRequest(memberAccount);
        Member member = memberRepository.findByEmail(memberAccount.getEmail()).orElseThrow(NotExistMemberException::new);
        member.verifyEmailMatch(memberAccount.getEmail());
        member.changeBirthDate(memberBirthDayUpdateRequest.getBirthDate());
    }

    @Transactional
    public void changePhone(MemberPhoneUpdateRequest memberPhoneUpdateRequest, MemberAccount memberAccount) {
        RightRequesterChecker.verifyLoginRequest(memberAccount);
        Member member = memberRepository.findByEmail(memberAccount.getEmail()).orElseThrow(NotExistMemberException::new);
        member.verifyEmailMatch(memberAccount.getEmail());
        member.changePhone(memberPhoneUpdateRequest.getPhone());
    }
}
