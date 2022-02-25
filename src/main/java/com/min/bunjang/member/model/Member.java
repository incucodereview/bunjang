package com.min.bunjang.member.model;

import com.min.bunjang.login.exception.NotMacheEmailAndPasswordException;
import com.min.bunjang.member.dto.MemberDirectCreateDto;
import com.min.bunjang.store.model.Store;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "member", orphanRemoval = true)
    private Store store;

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
        this.joinDate = joinDate;
        this.updatedDate = updatedDate;
        this.memberRole = memberRole;
    }

    public static Member createMember(MemberDirectCreateDto memberDirectCreateDto) {
        return Member.builder()
                .email(memberDirectCreateDto.getEmail())
                .password(memberDirectCreateDto.getPassword())
                .name(memberDirectCreateDto.getName())
                .phone(memberDirectCreateDto.getPhone())
                .birthDate(memberDirectCreateDto.getBirthDate())
                .joinDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .memberRole(memberDirectCreateDto.getMemberRole())
                .build();
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
