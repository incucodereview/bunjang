package com.min.bunjang.product.dto;

import com.min.bunjang.product.model.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductSimpleResponse {
    private Long productNum;
    private String productThumbnail;
    private String productName;
    private int productPrice;
    private LocalDateTime updateDateTime;
    private String exchangeLocation;

    public ProductSimpleResponse(Long productNum, String productThumbnail, String productName, int productPrice, LocalDateTime updateDateTime, String exchangeLocation) {
        this.productNum = productNum;
        this.productThumbnail = productThumbnail;
        this.productName = productName;
        this.productPrice = productPrice;
        this.updateDateTime = updateDateTime;
        this.exchangeLocation = exchangeLocation;
    }

    public static ProductSimpleResponse of(Product product) {
        return new ProductSimpleResponse(
                product.getNum(),
                null/* TODO product.getProductPhotos()*/,
                product.getProductName(),
                product.getProductPrice(),
                product.getUpdatedDate(),
                product.getExchangeLocation()
        );
    }

    public static List<ProductSimpleResponse> listOf(List<Product> productList) {
        return productList.stream()
                .map(ProductSimpleResponse::of)
                .collect(Collectors.toList());
    }
}
