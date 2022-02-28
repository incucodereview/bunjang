package com.min.bunjang.store.storeinquire.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InquireCreateResponse {
    private Long inquiryNum;
    private String writerName;
    private String inquiryContent;
}
