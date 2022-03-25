package com.min.bunjang.store.controller;

import com.min.bunjang.aws.s3.service.S3UploadService;
import com.min.bunjang.store.model.StoreThumbnail;
import com.min.bunjang.store.repository.StoreThumbnailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class StoreThumbnailUploadTestController {
    private final StoreThumbnailRepository storeThumbnailRepository;
    private final S3UploadService s3UploadService;

    @GetMapping("/store/file")
    public String getFileUploadPage() {
        return "/fileTest";
    }

    @PostMapping("/store/file")
    @ResponseBody
    public String saveFileUploadPage(String fileName, MultipartFile file) throws IOException {
        String filePath = s3UploadService.uploadForMultiFile(file);
        StoreThumbnail save = storeThumbnailRepository.save(StoreThumbnail.createStoreThumbnail(filePath));
        return filePath;
    }
}
