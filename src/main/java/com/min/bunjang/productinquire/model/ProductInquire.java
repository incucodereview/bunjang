package com.min.bunjang.productinquire.model;

import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductInquire extends BasicEntity {
    @NotNull
    private Long storeNum;

    @NotNull
    private Long productNum;

    @NotEmpty
    private String inquireContent;

    private Long inquireWriterNumForAnswer;

    private ProductInquire(Long storeNum, Long productNum, String inquireContent, Long inquireWriterNumForAnswer) {
        this.storeNum = storeNum;
        this.productNum = productNum;
        this.inquireContent = inquireContent;
        this.inquireWriterNumForAnswer = inquireWriterNumForAnswer;
    }

    public static ProductInquire createProductInquire(Long storeNum, Long productNum, String inquireContent, Long inquireWriterNumForAnswer) {
        return new ProductInquire(storeNum, productNum, inquireContent, inquireWriterNumForAnswer);
    }
}
