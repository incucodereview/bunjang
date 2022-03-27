package com.min.bunjang.store.model;

import com.min.bunjang.aws.s3.model.S3PhotoFile;
import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToOne(fetch = FetchType.LAZY)
    private Store store;

    public StoreThumbnail(String filePath, Store store) {
        this.filePath = filePath;
        this.store = store;
    }

    public static StoreThumbnail createStoreThumbnail(String filePath, Store store) {
        return new StoreThumbnail(filePath, store);
    }
}

