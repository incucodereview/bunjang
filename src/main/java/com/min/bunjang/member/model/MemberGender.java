package com.min.bunjang.member.model;

import lombok.Getter;

@Getter
public enum MemberGender {
    MEN("남자"),
    WOMEN("여자");

    private String desc;

    MemberGender(String desc) {
        this.desc = desc;
    }
}
