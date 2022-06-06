package com.min.bunjang.wishproduct.dto.response;

import com.min.bunjang.common.dto.PageDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WishProductResponses {
    private List<WishProductResponse> wishProductResponses;
    private PageDto pageDto;
}
