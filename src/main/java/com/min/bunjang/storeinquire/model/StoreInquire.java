package com.min.bunjang.storeinquire.model;

import com.min.bunjang.common.exception.WrongWriterException;
import com.min.bunjang.common.model.BasicEntity;
import com.min.bunjang.store.model.Store;
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

    @NotBlank
    private String content;

    private Long mentionedStoreNumForAnswer;
    private String mentionedStoreNameForAnswer;

    private StoreInquire(Long ownerNum, Store writer, String content) {
        this.ownerNum = ownerNum;
        this.writer = writer;
        this.content = content;
    }

    public static StoreInquire of(Long ownerNum, Store writer, String content) {
        return new StoreInquire(ownerNum, writer, content);
    }

    public void defineMention(Long mentionedStoreNumForAnswer, String mentionedStoreNameForAnswer) {
        this.mentionedStoreNumForAnswer = mentionedStoreNumForAnswer;
        this.mentionedStoreNameForAnswer = mentionedStoreNameForAnswer;
    }
}
