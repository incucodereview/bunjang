package com.min.bunjang.productinquire.dto;

import com.min.bunjang.aws.s3.dto.S3FileDto;
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
    private S3FileDto writerThumbnail;
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
                //TODO StoreThumbnail 의 fileName 값에 대한 필요성을 다시 생각하고 필요 없다면 null을 어떻게 처리할지 -> S3FileDto을 변형하든 새로운 파일 DTO를 만들든. 애초에 파일에 이름이 필요한가..?
                new S3FileDto(writer.getStoreThumbnail().getNum(), null, writer.getStoreThumbnail().getFilePath()),
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
