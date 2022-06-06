package com.min.bunjang.join.service;

import com.min.bunjang.join.confirmtoken.exception.WrongConfirmEmailToken;
import com.min.bunjang.join.confirmtoken.model.ConfirmationToken;
import com.min.bunjang.join.confirmtoken.repository.ConfirmationTokenRepository;
import com.min.bunjang.join.dto.JoinRequest;
import com.min.bunjang.join.event.JoinEmailEvent;
import com.min.bunjang.join.dto.TempJoinRequest;
import com.min.bunjang.join.exception.WrongCertificationJoinException;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.member.exception.NotExistTempMemberException;
import com.min.bunjang.member.model.JoinTempMember;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.model.MemberRole;
import com.min.bunjang.member.repository.JoinTempMemberRepository;
import com.min.bunjang.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class EmailJoinService {
    private final JoinTempMemberRepository joinTempMemberRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public void joinTempMember(TempJoinRequest tempJoinRequest) {
        JoinTempMember joinTempMember = JoinTempMember.createJoinTempMember(tempJoinRequest, bCryptPasswordEncoder);
        JoinTempMember savedJoinTempMember = joinTempMemberRepository.save(joinTempMember);

        eventPublisher.publishEvent(new JoinEmailEvent(this, savedJoinTempMember.getEmail()));
    }

    @Transactional
    public void joinMember(JoinRequest joinRequest) {
        JoinTempMember joinTempMember = joinTempMemberRepository.findById(joinRequest.getEmail()).orElseThrow(NotExistTempMemberException::new);
        Member member = Member.createMember(MemberDirectCreateDto.toMemberThroughTempMember(joinTempMember, MemberRole.ROLE_MEMBER, joinRequest.getMemberGender()));

        memberRepository.save(member);
        joinTempMemberRepository.delete(joinTempMember);
    }

    @Transactional
    public String verifyConfirmEmailToken(String token) {
        ConfirmationToken confirmationToken =
                confirmationTokenRepository.findByIdAndExpirationDateAfterAndExpired(token, LocalDateTime.now(), false).orElseThrow(WrongConfirmEmailToken::new);
        confirmationToken.useToken();
        return confirmationToken.getEmail();
    }
}
