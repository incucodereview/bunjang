package com.min.bunjang.trade.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.trade.dto.TradeCreateRequest;
import com.min.bunjang.trade.dto.TradeCreateResponse;
import com.min.bunjang.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TradeController {
    private final TradeService tradeService;

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PostMapping(TradeControllerPath.TRADE_CREATE)
    public RestResponse<TradeCreateResponse> createTrade(
            @Validated @RequestBody TradeCreateRequest tradeCreateRequest,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        TradeCreateResponse tradeCreateResponse = tradeService.createTrade(memberAccount.getEmail(), tradeCreateRequest);
        return RestResponse.of(HttpStatus.OK, tradeCreateResponse);
    }
}
