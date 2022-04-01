package com.min.bunjang.aws.s3.controller;

import com.min.bunjang.aws.s3.service.S3UploadService;
import com.min.bunjang.product.dto.ProductPhotoResponse;
import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductPhoto;
import com.min.bunjang.product.repository.ProductPhotoRepository;
import com.min.bunjang.product.repository.ProductRepository;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.model.StoreThumbnail;
import com.min.bunjang.store.repository.StoreRepository;
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
    private final StoreRepository storeRepository;
    private final StoreThumbnailRepository storeThumbnailRepository;
    private final ProductRepository productRepository;
    private final ProductPhotoRepository productPhotoRepository;
    private final S3UploadService s3UploadService;

    @GetMapping("/store/file")
    public String getFileUploadPage() {
        return "/fileTest";
    }

    @PostMapping("/store/file")
    @ResponseBody
    public String saveFileUploadPage(String fileName, MultipartFile file) throws IOException {
        String filePath = s3UploadService.uploadForMultiFile(file);
//        Store store = storeRepository.findById(1L).get();
//        StoreThumbnail save = storeThumbnailRepository.save(StoreThumbnail.createStoreThumbnail(filePath, store));
        Product product = productRepository.findById(2L).get();
        ProductPhoto productPhoto = productPhotoRepository.save(ProductPhoto.of(filePath, product));
        return filePath;
    }
}
