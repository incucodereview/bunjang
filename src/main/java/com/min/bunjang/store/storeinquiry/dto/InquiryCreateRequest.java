package com.min.bunjang.store.storeinquiry.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InquiryCreateRequest {
    private Long ownerNum;
    private Long writerNum;
    private String inquiryContent;
}
