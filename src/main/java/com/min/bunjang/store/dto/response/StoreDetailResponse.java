package com.min.bunjang.store.dto.response;

import com.min.bunjang.aws.s3.dto.S3FileDto;
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
    private S3FileDto storeThumbnail;
    private String storeName;
    private double storeScore;
    private Period openDate;
    private int hits;
    private String introduceContent;

    public static StoreDetailResponse of(Store store) {
        return new StoreDetailResponse(
                store.getNum(),
                //TODO StoreThumbnail 의 fileName 값에 대한 필요성을 다시 생각하고 필요 없다면 null을 어떻게 처리할지 -> S3FileDto을 변형하든 새로운 파일 DTO를 만들든. 애초에 파일에 이름이 필요한가..?
                new S3FileDto(store.getStoreThumbnail().getNum(), null, store.getStoreThumbnail().getFilePath()),
                store.getStoreName(),
                store.calculateAverageDealScore(),
                store.calculateOpenTime(),
                store.getHits(),
                store.getIntroduceContent()
        );
    }
}
