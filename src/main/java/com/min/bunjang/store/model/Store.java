package com.min.bunjang.store.model;

import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.member.model.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BasicEntity {

    private String storeName;

    private String introduceContent;


    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    private Store(String storeName, String introduceContent, Member member) {
        this.storeName = storeName;
        this.introduceContent = introduceContent;
        this.member = member;
    }

    public static Store createStore(String storeName, String introduceContent, Member member) {
        return new Store(storeName, introduceContent, member);
    }
}
