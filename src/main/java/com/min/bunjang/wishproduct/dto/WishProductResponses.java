package com.min.bunjang.wishproduct.dto;

import com.min.bunjang.common.dto.PageDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WishProductResponses {
    private List<WishProductResponse> wishProductResponses;
    private PageDto pageDto;
}
