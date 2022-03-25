package com.min.bunjang.aws.s3.controller;

import com.min.bunjang.aws.s3.service.S3UploadService;
import com.min.bunjang.common.dto.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class S3UploadController {
    private final S3UploadService s3UploadService;

    @PostMapping(S3UploadControllerPath.UPLOADS)
    public RestResponse<List<String>> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<String> uploadedFileAddresses = s3UploadService.uploads(multipartFiles);
        return RestResponse.of(HttpStatus.OK, uploadedFileAddresses);
    }

    @PostMapping(S3UploadControllerPath.UPLOAD)
    public RestResponse<String> uploadFile(MultipartFile multipartFile) throws IOException {
        String uploadedFileAddress = s3UploadService.uploadForMultiFile(multipartFile);
        return RestResponse.of(HttpStatus.OK, uploadedFileAddress);
    }
}
