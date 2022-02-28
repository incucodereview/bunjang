package com.min.bunjang.store.storeinquire.model;

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
public class StoreInquire extends BasicEntity {

    @NotNull
    private Long ownerNum;

    @NotNull
    private Long writerNum;

    private String writerThumbnail;

    @NotBlank
    private String content;

    // TODO: 섬네일은 s3 작업후 진행할 것. 일단 임시로 null처리
    private StoreInquire(Long ownerNum, Long writerNum, String writerThumbnail, String content) {
        this.ownerNum = ownerNum;
        this.writerNum = writerNum;
        this.writerThumbnail = null;
        this.content = content;
    }

    public static StoreInquire of(Long ownerNum, Long writerNum, String writerThumbnail, String content) {
        return new StoreInquire(ownerNum, writerNum, writerThumbnail, content);
    }
}
