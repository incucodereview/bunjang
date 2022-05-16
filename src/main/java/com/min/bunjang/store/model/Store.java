package com.min.bunjang.store.model;

import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.following.model.Following;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.dto.request.StoreCreateOrUpdateRequest;
import com.min.bunjang.storereview.model.StoreReview;
import com.min.bunjang.trade.model.Trade;
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
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BasicEntity {

    private String storeName;

    private String introduceContent;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "store", orphanRemoval = true)
    private StoreThumbnail storeThumbnail;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "visitor",
            joinColumns = @JoinColumn(name = "store_id"))
    private Set<Long> visitors = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "store", orphanRemoval = true)
    private List<Product> Products = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "store", orphanRemoval = true)
    private Set<WishProduct> wishProducts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
    private Set<StoreReview> storeReviews = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "followerStore", orphanRemoval = true)
    private List<Following> followings = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "followedStore", orphanRemoval = true)
    private List<Following> followers = new ArrayList<>();

    private int hits;

    private String contactableTime;
    private String exchangeAndReturnAndRefundPolicy;
    private String cautionNoteBeforeTrade;

    public Store(String storeName, String introduceContent, StoreThumbnail storeThumbnail, Member member) {
        this.storeName = storeName;
        this.introduceContent = introduceContent;
        this.storeThumbnail = storeThumbnail;
        this.member = member;
        this.hits = 0;
        this.contactableTime = "24시간";
        this.exchangeAndReturnAndRefundPolicy = "";
        this.cautionNoteBeforeTrade = "";
    }

    public static Store createStore(String storeName, String introduceContent, StoreThumbnail storeThumbnail, Member member) {
        return new Store(storeName, introduceContent, storeThumbnail, member);
    }

    public void updateStore(StoreCreateOrUpdateRequest storeCreateOrUpdateRequest) {
        this.storeName = storeCreateOrUpdateRequest.getStoreName();
        this.introduceContent = storeCreateOrUpdateRequest.getIntroduceContent();
        this.contactableTime = storeCreateOrUpdateRequest.getContactableTime();
        this.exchangeAndReturnAndRefundPolicy = storeCreateOrUpdateRequest.getExchangeAndReturnAndRefundPolicy();
        this.cautionNoteBeforeTrade = storeCreateOrUpdateRequest.getCautionNoteBeforeTrade();
    }

    public void updateThumbnail(StoreThumbnail storeThumbnail) {
        this.storeThumbnail = storeThumbnail;
    }

    public void updateIntroduceContent(String introduceContent) {
        this.introduceContent = introduceContent;
    }

    public void updateStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Period calculateOpenTime() {
        if (this.createdDate == null) {
            return null;
        }

        LocalDate now = LocalDate.now();
        return Period.between(createdDate.toLocalDate(), now);
    }

    public double calculateAverageDealScore() {
        if (this.storeReviews.isEmpty()) {
            return 0;
        }

        double average = 0;
        for (StoreReview storeReview : this.storeReviews) {
            average += storeReview.getDealScore();
        }
//TODO 방법별론거 같다. 평가할때 한번에 점수 적용하고 조회할땐 가지고 있는 점수를 반환하는게 좋은듯. 일단 점수 필드만들것.
        return average / this.storeReviews.size();
    }

    public void addHitsCount(MemberAccount email) {
        if (email == null) {
            return;
        }
        this.hits += 1;
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

    public boolean checkExistThumbnail() {
        return this.storeThumbnail != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return hits == store.hits && Objects.equals(storeName, store.storeName) && Objects.equals(introduceContent, store.introduceContent) && Objects.equals(member, store.member) && Objects.equals(storeThumbnail, store.storeThumbnail) && Objects.equals(visitors, store.visitors) && Objects.equals(Products, store.Products) && Objects.equals(wishProducts, store.wishProducts) && Objects.equals(storeReviews, store.storeReviews) && Objects.equals(followings, store.followings) && Objects.equals(followers, store.followers) && Objects.equals(contactableTime, store.contactableTime) && Objects.equals(exchangeAndReturnAndRefundPolicy, store.exchangeAndReturnAndRefundPolicy) && Objects.equals(cautionNoteBeforeTrade, store.cautionNoteBeforeTrade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeName, introduceContent, member, storeThumbnail, visitors, Products, wishProducts, storeReviews, followings, followers, hits, contactableTime, exchangeAndReturnAndRefundPolicy, cautionNoteBeforeTrade);
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeName='" + storeName + '\'' +
                ", introduceContent='" + introduceContent + '\'' +
                ", storeThumbnail=" + storeThumbnail +
                ", visitors=" + visitors +
                ", Products=" + Products +
                ", wishProducts=" + wishProducts +
                ", storeReviews=" + storeReviews +
                ", followings=" + followings +
                ", followers=" + followers +
                ", hits=" + hits +
                ", contactableTime='" + contactableTime + '\'' +
                ", exchangeAndReturnAndRefundPolicy='" + exchangeAndReturnAndRefundPolicy + '\'' +
                ", cautionNoteBeforeTrade='" + cautionNoteBeforeTrade + '\'' +
                '}';
    }
}
