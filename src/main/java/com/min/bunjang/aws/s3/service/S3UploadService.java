package com.min.bunjang.aws.s3.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.min.bunjang.aws.s3.proterties.AmazonS3BucketProperties;
import com.min.bunjang.aws.s3.proterties.AmazonS3CredentialsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3UploadService {
    private final AmazonS3CredentialsProperties amazonS3CredentialsProperties;
    private final AmazonS3BucketProperties amazonS3BucketProperties;
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(amazonS3CredentialsProperties.getAccessKey(), amazonS3CredentialsProperties.getSecretKey());
        amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    public List<String> uploads(List<MultipartFile> multipartFiles) throws IOException {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String originalFilename = multipartFile.getOriginalFilename();

            putS3(multipartFile, originalFilename);
            fileUrls.add(getThumbnailPath(originalFilename));
        }

        return fileUrls;
    }

    public String uploadForMultiFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        putS3(multipartFile, originalFilename);
        return getThumbnailPath(originalFilename);
    }

    public String getThumbnailPath(String path) {
        return String.valueOf(amazonS3.getUrl(amazonS3BucketProperties.getBucket(), path));
    }

    private void putS3(MultipartFile multipartFile, String originalFilename) throws IOException {
        amazonS3.putObject(new PutObjectRequest(amazonS3BucketProperties.getBucket(), originalFilename, multipartFile.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
