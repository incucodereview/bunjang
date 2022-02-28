package com.min.bunjang.helpers;

import com.min.bunjang.member.model.Member;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;

public class StoreAcceptanceHelper {

    public static Store 상점생성(Member member, StoreRepository storeRepository) {
        String storeName = "storeName";
        String introduceContent = "introduceContent";
        return storeRepository.save(Store.createStore(storeName, introduceContent, member));
    }
}
