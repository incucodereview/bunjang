package com.min.bunjang.join.service;

import com.min.bunjang.member.dto.EmailJoinRequest;
import com.min.bunjang.member.model.JoinTempMember;
import com.min.bunjang.member.repository.JoinTempMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class EmailJoinService {
    private final JoinTempMemberRepository joinTempMemberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinTempMember(EmailJoinRequest emailJoinRequest) {
        JoinTempMember joinTempMember = JoinTempMember.of(
                emailJoinRequest.getEmail(),
                bCryptPasswordEncoder.encode(emailJoinRequest.getPassword()),
                emailJoinRequest.getName(),
                emailJoinRequest.getPhone(),
                emailJoinRequest.getBirthDate()
        );
        joinTempMemberRepository.save(joinTempMember);


    }
}
