package com.min.bunjang.productinquire.dto.request;

import com.min.bunjang.product.common.ProductRequestValidatorMessages;
import com.min.bunjang.productinquire.common.ProductInquireRequestValidatorMessages;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductInquireCreateRequest {
    @NotNull(message = ProductInquireRequestValidatorMessages.PRODUCT_INQUIRE_BLANK_WRITER_NUM)
    private Long writerNum;
    @NotNull(message = ProductInquireRequestValidatorMessages.PRODUCT_BLANK_PRODUCT_NUM)
    private Long productNum;
    @NotBlank(message = ProductInquireRequestValidatorMessages.PRODUCT_BLANK_INQUIRE_CONTENT)
    private String inquireContent;
    private Long mentionedStoreNumForAnswer;

    public boolean isCheckExistenceMentionedStoreNum() {
        return this.mentionedStoreNumForAnswer != null;
    }
}
