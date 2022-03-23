package com.min.bunjang.aws.s3.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class S3PhotoFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoFileNum;

    @NotBlank
    private String fileName;

    @NotBlank
    private String filePath;

    private S3PhotoFile(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public static S3PhotoFile createS3PhotoFile(String fileName, String filePath) {
        return new S3PhotoFile(fileName, filePath);
    }
}
