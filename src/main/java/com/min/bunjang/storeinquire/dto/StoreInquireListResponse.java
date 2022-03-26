package com.min.bunjang.storeinquire.dto;

import com.min.bunjang.storeinquire.model.StoreInquire;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreInquireListResponse {
    private Long inquireNum;
    private String writerName;
    private Long writerThumbnail;
    private String inquireContent;
    private LocalDate postingDate;

    public StoreInquireListResponse(Long inquireNum, String writerName, Long writerThumbnail, String inquireContent, LocalDate postingDate) {
        this.inquireNum = inquireNum;
        this.writerName = writerName;
        this.writerThumbnail = writerThumbnail;
        this.inquireContent = inquireContent;
        this.postingDate = postingDate;
    }

    public static List<StoreInquireListResponse> listOf(List<StoreInquire> inquires) {
        return inquires.stream()
                .map(inquire -> new StoreInquireListResponse(
                        inquire.getNum(),
                        inquire.getWriter().getStoreName(),
                        inquire.getWriterThumbnail(),
                        inquire.getContent(),
                        inquire.getUpdatedDate().toLocalDate()))
                .collect(Collectors.toList());
    }
}
