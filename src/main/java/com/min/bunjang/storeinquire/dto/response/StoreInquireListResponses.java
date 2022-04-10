package com.min.bunjang.storeinquire.dto.response;

import com.min.bunjang.common.dto.PageDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreInquireListResponses {
    private List<StoreInquireResponse> storeInquireResponse;
    private PageDto pageDto;

    public StoreInquireListResponses(List<StoreInquireResponse> storeInquireResponse, PageDto pageDto) {
        this.storeInquireResponse = storeInquireResponse;
        this.pageDto = pageDto;
    }
}
