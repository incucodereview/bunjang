package com.min.bunjang.aws.s3.dto;

import com.min.bunjang.aws.s3.model.S3PhotoFile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class S3FileDto {
    private Long fileNum;
    private String fileName;
    private String fileLocation;

    public static S3FileDto of(Long fileNum, String fileName, String fileLocation) {
        return new S3FileDto(fileNum, fileName, fileLocation);
    }
}
