package com.min.bunjang.wishproduct.dto;

import com.min.bunjang.product.dto.ProductDetailResponse;
import com.min.bunjang.product.dto.ProductPhotoResponse;
import com.min.bunjang.wishproduct.model.WishProduct;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WishProductResponse {
    private Long wishProductNum;
    private Long productNum;
    private List<ProductPhotoResponse> productPhotos;
    private String productName;
    private int productPrice;
    private LocalDate postingDate;
    private String tradeArea;
    private String productState;

    public static WishProductResponse of(WishProduct wishProduct) {
        return new WishProductResponse(
                wishProduct.getNum(),
                wishProduct.getProduct().getNum(),
                ProductPhotoResponse.listOf(wishProduct.getProduct().getProductPhotos()),
                wishProduct.getProduct().getProductName(),
                1,
                wishProduct.getUpdatedDate().toLocalDate(),
                null,
                null
        );
    }

    public static List<WishProductResponse> listOf(List<WishProduct> wishProducts) {
        return wishProducts.stream()
                .map(WishProductResponse::of)
                .collect(Collectors.toList());
    }
}
