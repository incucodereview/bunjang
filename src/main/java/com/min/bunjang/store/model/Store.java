package com.min.bunjang.store.model;

import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.member.model.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BasicEntity {

    private String storeName;

    private String introduceContent;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "visitor",
            joinColumns = @JoinColumn(name = "store_id"))
    private Set<Long> visitors = new HashSet<>();

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
//    private Set<Visitor> visitors = new HashSet<>();

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

    public Period calculateOpenTime() {
        LocalDate now = LocalDate.now();
        return Period.between(createdDate.toLocalDate(), now);
    }

    public void plusVisitor(Long visitorNum) {
        if (visitors.contains(visitorNum)) {
            return;
        }
        this.getVisitors().add(visitorNum);
    }
}
