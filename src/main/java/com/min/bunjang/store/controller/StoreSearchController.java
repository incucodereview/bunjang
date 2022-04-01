package com.min.bunjang.store.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.store.dto.response.StoreSimpleResponses;
import com.min.bunjang.store.service.StoreSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreSearchController {
    private final StoreSearchService storeSearchService;

    @GetMapping(StoreSearchControllerPath.STORE_SEARCH_BY_KEYWORD)
    public RestResponse<StoreSimpleResponses> searchStoreByKeyword(
            @RequestParam("keyword") String keyword,
            @PageableDefault(sort = "num", direction = Sort.Direction.DESC, size = 85) Pageable pageable
    ) {

        StoreSimpleResponses storeSimpleResponses = storeSearchService.searchStoreByKeyword(keyword, pageable);
        return RestResponse.of(HttpStatus.OK, storeSimpleResponses);
    }
}
