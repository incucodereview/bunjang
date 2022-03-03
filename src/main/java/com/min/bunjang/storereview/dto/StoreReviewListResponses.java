package com.min.bunjang.storereview.dto;

import com.min.bunjang.common.dto.PageDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreReviewListResponses {
    private List<StoreReviewListResponse> storeReviewListResponses;
    private PageDto pageDto;
}
