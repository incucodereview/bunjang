package com.min.bunjang.product.dto;

import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductPhoto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductPhotoResponse {
    private Long photoNum;
    private String filePath;

    public static ProductPhotoResponse of(ProductPhoto productPhoto) {
        if (productPhoto == null) {
            return null;
        }

        return new ProductPhotoResponse(productPhoto.getNum(), productPhoto.getFilePath());
    }

    public static List<ProductPhotoResponse> listOf(List<ProductPhoto> productPhotos) {
        return productPhotos.stream()
                .map(ProductPhotoResponse::of)
                .collect(Collectors.toList());
    }
}
