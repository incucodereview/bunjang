package com.min.bunjang.join.service;

import com.min.bunjang.join.event.JoinEmailEvent;
import com.min.bunjang.member.dto.EmailJoinRequest;
import com.min.bunjang.member.model.JoinTempMember;
import com.min.bunjang.member.repository.JoinTempMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailJoinService {
    private final JoinTempMemberRepository joinTempMemberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public void joinTempMember(EmailJoinRequest emailJoinRequest) {
        JoinTempMember joinTempMember = JoinTempMember.createJoinTempMember(
                emailJoinRequest.getEmail(),
                bCryptPasswordEncoder.encode(emailJoinRequest.getPassword()),
                emailJoinRequest.getName(),
                emailJoinRequest.getPhone(),
                emailJoinRequest.getBirthDate()
        );
        JoinTempMember savedJoinTempMember = joinTempMemberRepository.save(joinTempMember);

        eventPublisher.publishEvent(new JoinEmailEvent(this, savedJoinTempMember.getEmail()));
    }
}
