package com.min.bunjang.storereview.model;

import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.model.StoreThumbnail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreReview extends BasicEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Store owner;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store writer;

    @NotBlank
    private String writerName;

    @NotNull
    private double dealScore;

    @NotNull
    private Long productNum;

    private String productName;

    @NotBlank
    private String reviewContent;

    public StoreReview(Store owner, Store writer, String writerName, double dealScore, Long productNum, String productName, String reviewContent) {
        this.owner = owner;
        this.writer = writer;
        this.writerName = writerName;
        this.dealScore = dealScore;
        this.storeThumbnailNum = storeThumbnail;
        this.productNum = productNum;
        this.productName = productName;
        this.reviewContent = reviewContent;
    }

    public static StoreReview createStoreReview(Store owner, Store writer, String writerName, double dealScore, Long storeThumbnail, Long productNum, String productName, String reviewContent) {
        return new StoreReview(
                owner,
                writer,
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

    public boolean verifyWriter(Store writer) {
        return this.writer.equals(writer);
    }

    public void defineStoreThumbnailNum(StoreThumbnail storeThumbnail) {
        if (storeThumbnail == null) {
            return;
        }

        this.storeThumbnailNum = storeThumbnail.getNum();
    }
}
