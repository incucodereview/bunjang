package com.min.bunjang.store.model;

import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.storereview.model.StoreReview;
import com.min.bunjang.wishproduct.model.WishProduct;
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
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BasicEntity {

    private String storeName;

    private String introduceContent;

    private String storeThumbnail;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "visitor",
            joinColumns = @JoinColumn(name = "store_id"))
    private Set<Long> visitors = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "store", orphanRemoval = true)
    private List<Product> Products = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "store", orphanRemoval = true)
    private Set<WishProduct> wishProducts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ownerNum", orphanRemoval = true)
    private Set<StoreReview> storeReviews = new HashSet<>();


    public Store(String storeName, String introduceContent, String storeThumbnail, Member member) {
        this.storeName = storeName;
        this.introduceContent = introduceContent;
        this.storeThumbnail = storeThumbnail;
        this.member = member;
    }

    public static Store createStore(String storeName, String introduceContent, String storeThumbnail, Member member) {
        return new Store(storeName, introduceContent, storeThumbnail, member);
    }

    public void updateIntroduceContent(String introduceContent) {
        this.introduceContent = introduceContent;
    }

    public void updateStoreName(String storeName) {
        this.storeName = storeName;
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

    public boolean verifyMatchMember(String memberEmail) {
        if (this.member == null) {
            throw new ImpossibleException("상점 데이터에 회원 정보가 존재하지 않습니다. 심각한 예외입니다.");
        }

        return this.member.getEmail().equals(memberEmail);
    }
}
