package com.min.bunjang.store.controller;

import com.min.bunjang.aws.s3.service.S3UploadService;
import com.min.bunjang.store.repository.StoreThumbnailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreThumbnailUploadController {
    private final StoreThumbnailRepository storeThumbnailRepository;
    private final S3UploadService s3UploadService;

    //TODO : ?? 삭제 해도 되나?
//    @PostMapping(StoreThumbnailUploadControllerPath.THUMBNAIL_UPLOAD)
//    public String saveFileUploadPage(String fileName, MultipartFile file) throws IOException {
//        String filePath = s3UploadService.uploadForMultiFile(file);
//        StoreThumbnail save = storeThumbnailRepository.save(StoreThumbnail.createStoreThumbnail(fileName, filePath));
//        return filePath;
//    }
}
