package com.min.bunjang.following.repository;

import com.min.bunjang.following.model.Following;
import com.min.bunjang.following.model.QFollowing;
import com.min.bunjang.product.model.QProduct;
import com.min.bunjang.store.model.QStore;
import com.min.bunjang.store.model.QStoreThumbnail;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.storereview.model.QStoreReview;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.min.bunjang.following.model.QFollowing.following;
import static com.min.bunjang.product.model.QProduct.product;
import static com.min.bunjang.store.model.QStore.store;
import static com.min.bunjang.store.model.QStoreThumbnail.storeThumbnail;
import static com.min.bunjang.storereview.model.QStoreReview.storeReview;

@Repository
@RequiredArgsConstructor
public class FollowingViewRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Slice<Following> findByFollowedStoreNum(Long storeNum, Pageable pageable) {
        List<Following> followingList = jpaQueryFactory.select(following)
                .from(following)
                .distinct()
                .leftJoin(following.followedStore, store).fetchJoin()
                .leftJoin(store.storeThumbnail, storeThumbnail).fetchJoin()
                .leftJoin(store.Products, product).fetchJoin()
                .leftJoin(store.storeReviews, storeReview).fetchJoin()
                .where(following.followedStore.num.eq(storeNum))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(following.updatedDate.desc())
                .fetch();

        return new PageImpl<>(followingList, pageable, followingList.size());
    }
}
