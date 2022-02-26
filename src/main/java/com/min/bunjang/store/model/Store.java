package com.min.bunjang.store.model;

import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.member.model.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.HashSet;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BasicEntity {

    private String storeName;

    private String introduceContent;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

//    private Set<Long> visitorNums = new HashSet<>();

    private Store(String storeName, String introduceContent, Member member) {
        this.storeName = storeName;
        this.introduceContent = introduceContent;
        this.member = member;
    }

    public static Store createStore(String storeName, String introduceContent, Member member) {
        return new Store(storeName, introduceContent, member);
    }

    public void updateIntroduceContent(String introduceContent) {
        this.introduceContent = introduceContent;
    }

//    public void plusVisitor(Long visitorNum) {
//    }

    public Period calculateOpenTime() {
        LocalDate now = LocalDate.now();
        return Period.between(createdDate.toLocalDate(), now);
    }
}
