package com.min.bunjang.storeinquire.dto.request;

import com.min.bunjang.storeinquire.common.StoreInquireRequestValidatorMessages;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InquireCreateRequest {
    @NotNull(message = StoreInquireRequestValidatorMessages.STORE_INQUIRE_BLANK_OWNER_NUM)
    private Long ownerNum;
    @NotNull(message = StoreInquireRequestValidatorMessages.STORE_INQUIRE_BLANK_WRITER_NUM)
    private Long writerNum;
    private String inquireContent;
    private Long mentionedStoreNumForAnswer;

    public boolean checkExistenceMentionedStoreNum() {
        return this.mentionedStoreNumForAnswer != null;
    }
}
