package com.min.bunjang.security;

import com.min.bunjang.member.model.Member;

import java.util.Arrays;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;


@Getter
public class MemberAccount extends User {

    private Member member;

    public MemberAccount(Member member) {
        super(member.getEmail(), member.getPassword(), Arrays.asList(new SimpleGrantedAuthority(String.valueOf(member.getMemberRole()))));
        this.member = member;
    }

    public String getEmail() {
        String username = member.getEmail();
        if (username == null) {
            return null;
        }
        return username;
    }

    //Member memberFetch = Optional.ofNullable(memberDetails)
    //                .map(m -> memberQueryRepository.findByEmailFetch(m.getEmail()))
    //                .orElse(null);
}
