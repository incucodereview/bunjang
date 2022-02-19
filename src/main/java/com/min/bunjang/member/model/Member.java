package com.min.bunjang.member.model;

import com.min.bunjang.login.dto.LoginRequest;
import com.min.bunjang.login.exception.NotMacheEmailAndPasswordException;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "Member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNum;

    @NotBlank
    private String email;
    @NotBlank
    private String password;

    private String name;
    private String phone;

    private LocalDate birthDate;
    private LocalDateTime joinDate;
    private LocalDateTime updatedDate;

    private MemberRole memberRole;

    @Builder(access = AccessLevel.PRIVATE)
    public Member(Long memberNum,
                  String email,
                  String password,
                  String name,
                  String phone,
                  LocalDate birthDate,
                  LocalDateTime joinDate,
                  LocalDateTime updatedDate,
                  MemberRole memberRole) {
        this.memberNum = memberNum;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birthDate = birthDate;
        this.memberRole = memberRole;
    }

    public static Member createMember(MemberDirectCreateDto memberDirectCreateDto) {
        return new Member(
                memberDirectCreateDto.getMemberNum(),
                memberDirectCreateDto.getEmail(),
                memberDirectCreateDto.getPassword(),
                memberDirectCreateDto.getName(),
                memberDirectCreateDto.getPhone(),
                memberDirectCreateDto.getBirthDate(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                memberDirectCreateDto.getMemberRole()
        );
    }

    public void verifyMatchPassword(String inputPassword, BCryptPasswordEncoder bCryptPasswordEncoder) {
        if (!bCryptPasswordEncoder.matches(inputPassword, password)) {
            throw new NotMacheEmailAndPasswordException();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(memberRole.name()));
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
