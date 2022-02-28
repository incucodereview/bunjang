package com.min.bunjang.storeinquire.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InquireCreateRequest {
    private Long ownerNum;
    private Long writerNum;
    private String inquireContent;
}
