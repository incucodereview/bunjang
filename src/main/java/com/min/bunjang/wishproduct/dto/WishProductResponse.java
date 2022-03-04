package com.min.bunjang.wishproduct.dto;

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
    private String productThumbnail;
    private String productName;
    private int productPrice;
    private LocalDate postingDate;
    private String tradeArea;
    //TODO 조회 끝난후 상품 엔티티구성시 수정되야 할 영역
    private String productState;

    public static WishProductResponse of(WishProduct wishProduct) {
        return new WishProductResponse(
                wishProduct.getNum(),
                wishProduct.getProduct().getNum(),
                //TODO 여기도... 수정 아예 상품에 섬네일 필드가 없음
                null,
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
