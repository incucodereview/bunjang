package com.min.bunjang.aws.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3UploadService {
    private final AmazonS3CredentialsProperties amazonS3CredentialsProperties;


}
