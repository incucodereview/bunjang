package com.min.bunjang.store.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.dto.response.StoreDetailResponse;
import com.min.bunjang.store.service.StoreViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StoreViewController {
    private final StoreViewService storeViewService;

    @GetMapping(StoreViewControllerPath.STORE_FIND)
    public RestResponse<StoreDetailResponse> findStore(
            @NotNull @PathVariable Long storeNum,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        StoreDetailResponse storeDetailResponse = storeViewService.findStore(memberAccount, storeNum);
        return RestResponse.of(HttpStatus.OK, storeDetailResponse);
    }
}
