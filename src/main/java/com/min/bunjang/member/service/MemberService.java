package com.min.bunjang.member.service;

import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberGender;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.security.MemberAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void changeGender(Long memberNum, MemberGender memberGender, MemberAccount memberAccount) {
        Member member = memberRepository.findById(memberNum).orElseThrow(NotExistMemberException::new);
        member.verifyEmailMatch(memberAccount.getEmail());
        member.changeGender(memberGender);
    }
}
