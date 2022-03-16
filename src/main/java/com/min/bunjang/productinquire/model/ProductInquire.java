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
    private Long writerNum;

    @NotNull
    private Long productNum;

    @NotEmpty
    private String inquireContent;

    private Long inquireWriterNumForAnswer;

    private ProductInquire(Long writerNum, Long productNum, String inquireContent, Long inquireWriterNumForAnswer) {
        this.writerNum = writerNum;
        this.productNum = productNum;
        this.inquireContent = inquireContent;
        this.inquireWriterNumForAnswer = inquireWriterNumForAnswer;
    }

    public static ProductInquire createProductInquire(Long writerNum, Long productNum, String inquireContent, Long inquireWriterNumForAnswer) {
        return new ProductInquire(writerNum, productNum, inquireContent, inquireWriterNumForAnswer);
    }
}
