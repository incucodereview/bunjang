package com.min.bunjang.storereview.dto.response;

import com.min.bunjang.common.dto.PageDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreReviewListResponses {
    private List<StoreReviewResponse> storeReviewListResponses;
    private PageDto pageDto;
}
