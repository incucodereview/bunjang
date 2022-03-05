package com.min.bunjang.member.model;

public enum MemberRole {
    ROLE_ADMIN("어드민 회원"),
    ROLE_MEMBER("일반 회원");

    private String desc;

    MemberRole(String desc) {
        this.desc = desc;
    }
}
