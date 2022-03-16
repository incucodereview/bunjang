package com.min.bunjang.productinquire.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductInquireCreateRequest {
    @NotNull
    private Long writerNum;
    @NotNull
    private Long productNum;
    @NotEmpty
    private String inquireContent;
    private Long mentionedStoreNumForAnswer;

    public boolean isCheckExistenceMentionedStoreNum() {
        return this.mentionedStoreNumForAnswer != null;
    }
}
