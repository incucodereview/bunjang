package com.min.bunjang.storeinquire.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InquireCreateResponse {
    private Long inquireNum;
    private String writerName;
    private String inquireContent;
}
