package com.min.bunjang.member.dto;

import com.min.bunjang.member.model.MemberRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberDirectCreateDto {
    private Long memberNum;
    private String email;
    private String password;
    private String name;
    private String phone;
    private LocalDate birthDate;
    private MemberRole memberRole;
}
