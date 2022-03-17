package com.min.bunjang.productinquire.dto;

import com.min.bunjang.productinquire.model.ProductInquire;
import com.min.bunjang.store.model.Store;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductInquireResponse {
    private Long inquireNum;
    private Long productNum;
    private Long writerNum;
    private String writerName;
    private String writerThumbnail;
    private String inquireContent;
    private LocalDateTime createDate;
    private Long answeredStoreNum;
    private String answeredStoreName;

    public static ProductInquireResponse of(ProductInquire productInquire, Store writer) {
        return new ProductInquireResponse(
                productInquire.getNum(),
                productInquire.getProductNum(),
                writer.getNum(),
                writer.getStoreName(),
                writer.getStoreThumbnail(),
                productInquire.getInquireContent(),
                productInquire.getUpdatedDate(),
                Optional.ofNullable(productInquire.getMentionedStoreNumForAnswer()).orElse(null),
                Optional.ofNullable(productInquire.getMentionedStoreNameForAnswer()).orElse(null)
        );
    }

    public static List<ProductInquireResponse> listOf(List<ProductInquire> productInquires, Store writer) {
        return productInquires.stream()
                .map(productInquire -> ProductInquireResponse.of(productInquire, writer))
                .collect(Collectors.toList());
    }
}
