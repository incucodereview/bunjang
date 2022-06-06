package com.min.bunjang.store.model;

import com.min.bunjang.aws.s3.model.S3PhotoFile;
import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.awt.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreThumbnail extends BasicEntity {
    @NotBlank
    private String filePath;

    @NotNull
    private Long storeNum;

    public StoreThumbnail(String filePath, Long storeNum) {
        this.filePath = filePath;
        this.storeNum = storeNum;
    }

    public static StoreThumbnail createStoreThumbnail(String filePath, Long storeNum) {
        return new StoreThumbnail(filePath, storeNum);
    }
}

