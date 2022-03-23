package com.min.bunjang.aws.s3.controller;

import com.min.bunjang.aws.s3.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class S3UploadController {
    private final S3UploadService s3UploadService;


}
