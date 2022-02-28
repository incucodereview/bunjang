package com.min.bunjang.store.storeinquiry.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InquiryCreateResponse {
    private Long inquiryNum;
    private String writerName;
    private String inquiryContent;
}
