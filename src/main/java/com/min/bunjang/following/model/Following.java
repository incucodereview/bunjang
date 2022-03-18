package com.min.bunjang.following.model;

import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.store.model.Store;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Following extends BasicEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Store followerStore;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store followedStore;

    private Following(Store followerStore, Store followedStore) {
        this.followerStore = followerStore;
        this.followedStore = followedStore;
    }

    public static Following createFollowing(Store followerStore, Store followedStore) {
        return new Following(followerStore, followedStore);
    }

    public void defineRelationFollower(Store followerStore) {
        followerStore.getFollowings().add(this);
    }

    public void defineRelationFollowed(Store followedStore) {
        followedStore.getFollowers().add(this);
    }
}
