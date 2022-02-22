package com.min.bunjang.member.dto;

import com.min.bunjang.member.model.JoinTempMember;
import com.min.bunjang.member.model.MemberRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDirectCreateDto {
    private String email;
    private String password;
    private String name;
    private String phone;
    private LocalDate birthDate;
    private MemberRole memberRole;

    private MemberDirectCreateDto(String email, String password, String name, String phone, LocalDate birthDate, MemberRole memberRole) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birthDate = birthDate;
        this.memberRole = memberRole;
    }

    public static MemberDirectCreateDto of(String email, String password, String name, String phone, LocalDate birthDate, MemberRole memberRole) {
        return new MemberDirectCreateDto(
                email,
                password,
                name,
                phone,
                birthDate,
                memberRole
        );
    }

    public static MemberDirectCreateDto toMemberThroughTempMember(JoinTempMember joinTempMember, MemberRole memberRole) {
        return new MemberDirectCreateDto(
                joinTempMember.getEmail(),
                joinTempMember.getPassword(),
                joinTempMember.getName(),
                joinTempMember.getPhone(),
                joinTempMember.getBirthDate(),
                memberRole
        );
    }
}
