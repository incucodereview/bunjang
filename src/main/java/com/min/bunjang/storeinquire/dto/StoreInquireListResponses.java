package com.min.bunjang.storeinquire.dto;

import com.min.bunjang.common.dto.PageDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreInquireListResponses {
    private List<StoreInquireListResponse> storeInquireListResponse;
    private PageDto pageDto;

    public StoreInquireListResponses(List<StoreInquireListResponse> storeInquireListResponse, PageDto pageDto) {
        this.storeInquireListResponse = storeInquireListResponse;
        this.pageDto = pageDto;
    }
}
