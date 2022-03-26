package com.min.bunjang.store.dto.response;

import com.min.bunjang.aws.s3.dto.S3FileDto;
import com.min.bunjang.store.model.Store;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreThumbnailResponse {
    private Long fileNum;
    private String fileLocation;

    public static StoreThumbnailResponse of(Store store) {
        if (!store.checkExistThumbnail()) {
            return null;
        }

        return new StoreThumbnailResponse(store.getStoreThumbnail().getNum(), store.getStoreThumbnail().getFilePath());
    }
}
