package com.min.bunjang.security;

import com.min.bunjang.member.model.Member;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;


@Getter
public class MemberAccount extends User {

    private Long memberNum;
    private String email;
    private String memberName;

    public MemberAccount(Member member) {
        super(member.getEmail(), member.getPassword(), Arrays.asList(new SimpleGrantedAuthority(String.valueOf(member.getMemberRole()))));
        this.memberNum = member.getMemberNum();
        this.email = member.getEmail();
        this.memberName = member.getName();
    }


    public Long getMemberNum() {
        return Optional.ofNullable(memberNum).orElse(null);
    }

    public String getEmail() {
        return Optional.ofNullable(email).orElse(null);
    }

    public String getMemberName() {
        return Optional.ofNullable(memberName).orElse(null);
    }

    //Member memberFetch = Optional.ofNullable(memberDetails)
    //                .map(m -> memberQueryRepository.findByEmailFetch(m.getEmail()))
    //                .orElse(null);
}
