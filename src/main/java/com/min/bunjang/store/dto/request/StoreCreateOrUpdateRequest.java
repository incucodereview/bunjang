package com.min.bunjang.store.dto.request;

import com.min.bunjang.store.common.StoreRequestValidatorMessages;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreCreateOrUpdateRequest {
    @NotBlank(message = StoreRequestValidatorMessages.STORE_BLANK_NAME)
    private String storeName;
    private String introduceContent;
    private MultipartFile storeThumbnail;
    private String contactableTime;
    private String exchangeAndReturnAndRefundPolicy;
    private String cautionNoteBeforeTrade;
}
