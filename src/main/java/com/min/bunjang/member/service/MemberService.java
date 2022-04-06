package com.min.bunjang.member.service;

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

    @Transactional
    public void changeGender(Long memberNum, MemberGenderUpdateRequest memberGenderUpdateRequest, MemberAccount memberAccount) {
        Member member = memberRepository.findById(memberNum).orElseThrow(NotExistMemberException::new);
        member.verifyEmailMatch(memberAccount.getEmail());
        member.changeGender(memberGenderUpdateRequest.getMemberGender());
    }

    @Transactional
    public void changeBirthDay(Long memberNum, MemberBirthDayUpdateRequest memberBirthDayUpdateRequest, MemberAccount memberAccount) {
        Member member = memberRepository.findById(memberNum).orElseThrow(NotExistMemberException::new);
        member.verifyEmailMatch(memberAccount.getEmail());
        member.changeBirthDate(memberBirthDayUpdateRequest.getBirthDate());
    }

    @Transactional
    public void changePhone(Long memberNum, MemberPhoneUpdateRequest memberPhoneUpdateRequest, MemberAccount memberAccount) {
        Member member = memberRepository.findById(memberNum).orElseThrow(NotExistMemberException::new);
        member.verifyEmailMatch(memberAccount.getEmail());
        member.changePhone(memberPhoneUpdateRequest.getPhone());
    }
}
