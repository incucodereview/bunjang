package com.min.bunjang.productinquire.model;

import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductInquire extends BasicEntity {
    @NotNull
    private Long writerNum;

    @NotBlank
    private String writerName;

    @NotNull
    private Long productNum;

    @NotEmpty
    private String inquireContent;

    //TODO 맨션 관련 추가 테스트 필요.
    private Long mentionedStoreNumForAnswer;
    private String mentionedStoreNameForAnswer;

    private ProductInquire(Long writerNum, String writerName, Long productNum, String inquireContent) {
        this.writerNum = writerNum;
        this.writerName = writerName;
        this.productNum = productNum;
        this.inquireContent = inquireContent;
    }

    public static ProductInquire createProductInquire(Long writerNum, String writerName, Long productNum, String inquireContent) {
        return new ProductInquire(writerNum, writerName, productNum, inquireContent);
    }

    public void defineMention(Long mentionedStoreNumForAnswer, String mentionedStoreNameForAnswer) {
        this.mentionedStoreNumForAnswer = mentionedStoreNumForAnswer;
        this.mentionedStoreNameForAnswer = mentionedStoreNameForAnswer;
    }
}
