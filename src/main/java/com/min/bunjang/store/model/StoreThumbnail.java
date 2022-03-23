package com.min.bunjang.store.model;

import com.min.bunjang.aws.s3.model.S3PhotoFile;
import com.min.bunjang.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreThumbnail extends BasicEntity {
    @NotBlank
    private String fileName;

    @NotBlank
    private String filePath;

    private StoreThumbnail(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public static StoreThumbnail createStoreThumbnail(String fileName, String filePath) {
        return new StoreThumbnail(fileName, filePath);
    }
}

