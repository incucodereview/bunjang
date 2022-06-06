package com.min.bunjang.store.dto.response;

import com.min.bunjang.store.model.Store;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreCreateResponse {
    private Long storeId;
    private String storeName;
    private String introduceContent;

    public static StoreCreateResponse of(Store store) {
        return new StoreCreateResponse(
                store.getNum(),
                store.getStoreName(),
                store.getIntroduceContent()
        );
    }
}
