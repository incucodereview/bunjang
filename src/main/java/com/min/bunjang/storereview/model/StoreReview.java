package com.min.bunjang.storereview.model;

import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreReview extends BasicEntity {

    @NotNull
    private Long ownerNum;

    @NotNull
    private Long writerNum;

    @NotBlank
    private String writerName;

    @NotNull
    private double dealScore;

    private String storeThumbnail;

    @NotNull
    private Long productNum;

    private String productName;

    @NotBlank
    private String reviewContent;

    public StoreReview(Long ownerNum, Long writerNum, String writerName, double dealScore, String storeThumbnail, Long productNum, String productName, String reviewContent) {
        this.ownerNum = ownerNum;
        this.writerNum = writerNum;
        this.writerName = writerName;
        this.dealScore = dealScore;
        this.storeThumbnail = storeThumbnail;
        this.productNum = productNum;
        this.productName = productName;
        this.reviewContent = reviewContent;
    }

    public static StoreReview createStoreReview(Long ownerNum, Long writerNum, String writerName, double dealScore, String storeThumbnail, Long productNum, String productName, String reviewContent) {
        return new StoreReview(
                ownerNum,
                writerNum,
                writerName,
                dealScore,
                storeThumbnail,
                productNum,
                productName,
                reviewContent
        );
    }

    public void updateReviewContent(String reviewContent, double dealScore) {
        if (reviewContent == null) {
            throw new ImpossibleException("변경하려는 상점후기 내용이 없습니다.");
        }

        if (dealScore < 0 || dealScore > 5) {
            throw new ImpossibleException("변경하려는 후기점수가 부족하거나 초과합니다. 잘못된 접근입니다.");
        }

        this.dealScore = dealScore;
        this.reviewContent = reviewContent;
    }

    public boolean verifyWriter(Long rowWriterNum) {
        return this.writerNum.equals(writerNum);
    }
}
