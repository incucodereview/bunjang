package com.min.bunjang.member.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinTempMember {

    @Id
    private String email;
    private String password;

    private String name;
    private String phone;

    private LocalDate birthDate;
    private LocalDateTime joinDate;
    private LocalDateTime updatedDate;

    private JoinTempMember(String email, String password, String name, String phone, LocalDate birthDate) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birthDate = birthDate;
        this.joinDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public static JoinTempMember createJoinTempMember(String email, String password, String name, String phone, LocalDate birthDate) {
        return new JoinTempMember(
                email,
                password,
                name,
                phone,
                birthDate
        );
    }
}
