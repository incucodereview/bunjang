package com.min.bunjang.store.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreCreateOrUpdateRequest {
    private String storeName;
    private String introduceContent;
    private MultipartFile storeThumbnail;
    private String contactableTime;
    private String exchangeAndReturnAndRefundPolicy;
    private String cautionNoteBeforeTrade;
}
