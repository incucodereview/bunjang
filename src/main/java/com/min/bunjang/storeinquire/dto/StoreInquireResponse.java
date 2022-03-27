package com.min.bunjang.storeinquire.dto;

import com.min.bunjang.store.dto.response.StoreThumbnailResponse;
import com.min.bunjang.storeinquire.model.StoreInquire;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreInquireResponse {
    private Long inquireNum;
    private String writerName;
    private StoreThumbnailResponse writerThumbnail;
    private String inquireContent;
    private LocalDate postingDate;

    public StoreInquireResponse(Long inquireNum, String writerName, StoreThumbnailResponse writerThumbnail, String inquireContent, LocalDate postingDate) {
        this.inquireNum = inquireNum;
        this.writerName = writerName;
        this.writerThumbnail = writerThumbnail;
        this.inquireContent = inquireContent;
        this.postingDate = postingDate;
    }

    public static List<StoreInquireResponse> listOf(List<StoreInquire> inquires) {
        return inquires.stream()
                .map(inquire -> new StoreInquireResponse(
                        inquire.getNum(),
                        inquire.getWriter().getStoreName(),
                        StoreThumbnailResponse.of(inquire.getWriter()),
                        inquire.getContent(),
                        inquire.getUpdatedDate().toLocalDate()))
                .collect(Collectors.toList());
    }
}
