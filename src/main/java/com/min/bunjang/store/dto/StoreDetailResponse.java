package com.min.bunjang.store.dto;

import com.min.bunjang.store.model.Store;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Period;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreDetailResponse {
    private Long storeNum;
    private String storeThumbnail;
    private String storeName;
    private double storeScore;
    private Period openDate;
    private int hits;
    private String introduceContent;

    public static StoreDetailResponse of(Store store) {
        return new StoreDetailResponse(
                store.getNum(),
                store.getStoreThumbnail(),
                store.getStoreName(),
                store.calculateAverageDealScore(),
                store.calculateOpenTime(),
                store.getHits(),
                store.getIntroduceContent()
        );
    }
}
