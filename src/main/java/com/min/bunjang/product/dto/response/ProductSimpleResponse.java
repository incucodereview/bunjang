package com.min.bunjang.product.dto.response;

import com.min.bunjang.product.model.Product;
import com.min.bunjang.product.model.ProductTradeState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductSimpleResponse {
    private Long productNum;
    private List<ProductPhotoResponse> productPhotos;
    private String productName;
    private int productPrice;
    private LocalDateTime updateDateTime;
    private String exchangeLocation;
    private ProductTradeState productTradeState;

    public static ProductSimpleResponse of(Product product) {
        return new ProductSimpleResponse(
                product.getNum(),
                ProductPhotoResponse.listOf(product.getProductPhotos()),
                product.getProductName(),
                product.getProductPrice(),
                product.getUpdatedDate(),
                product.getTradeLocation(),
                product.getProductTradeState()
        );
    }

    public static List<ProductSimpleResponse> listOf(List<Product> productList) {
        return productList.stream()
                .map(ProductSimpleResponse::of)
                .collect(Collectors.toList());
    }
}
