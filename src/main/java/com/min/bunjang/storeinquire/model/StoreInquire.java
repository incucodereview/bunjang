package com.min.bunjang.storeinquire.model;

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
public class StoreInquire extends BasicEntity {

    @NotNull
    private Long ownerNum;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store writer;

    private Long writerThumbnail;

    @NotBlank
    private String content;

    //TODO 맨션 관련 추가 테스트 필요.
    private Long mentionedStoreNumForAnswer;
    private String mentionedStoreNameForAnswer;

    // TODO: 섬네일은 s3 작업후 진행할 것. 일단 임시로 null처리
    private StoreInquire(Long ownerNum, Store writer, Long writerThumbnail, String content) {
        this.ownerNum = ownerNum;
        this.writer = writer;
        this.writerThumbnail = writerThumbnail;
        this.content = content;
    }

    public static StoreInquire of(Long ownerNum, Store writer, Long writerThumbnail, String content) {
        return new StoreInquire(ownerNum, writer, writerThumbnail, content);
    }

    public void defineMention(Long mentionedStoreNumForAnswer, String mentionedStoreNameForAnswer) {
        this.mentionedStoreNumForAnswer = mentionedStoreNumForAnswer;
        this.mentionedStoreNameForAnswer = mentionedStoreNameForAnswer;
    }

    //TODO StoreReview와 로직 중복.
    public void defineStoreThumbnailNum(StoreThumbnail storeThumbnail) {
        if (storeThumbnail == null) {
            return;
        }

        this.writerThumbnail = storeThumbnail.getNum();
    }
}
