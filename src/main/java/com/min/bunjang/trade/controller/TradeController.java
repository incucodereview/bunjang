package com.min.bunjang.trade.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.product.exception.NotExistProductException;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.storereview.exception.NotExistStoreReviewException;
import com.min.bunjang.trade.dto.TradeCreateRequest;
import com.min.bunjang.trade.dto.TradeCreateResponse;
import com.min.bunjang.trade.exception.NotExistTradeException;
import com.min.bunjang.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

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

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @PatchMapping(TradeControllerPath.TRADE_COMPLETE)
    public RestResponse<Void> completeTrade(
            @NotNull @PathVariable Long tradeNum,
            @AuthenticationPrincipal MemberAccount memberAccount
    ) {
        tradeService.completeTrade(memberAccount.getEmail(), tradeNum);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    @DeleteMapping(TradeControllerPath.TRADE_CANCEL)
    public RestResponse<Void> cancelTrade(
            @AuthenticationPrincipal MemberAccount memberAccount,
            @NotNull @PathVariable Long tradeNum
    ) {
        tradeService.cancelTrade(memberAccount.getEmail(), tradeNum);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistStoreException.class)
    public RestResponse<Void> notExistStoreExceptionHandler(NotExistStoreException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistProductException.class)
    public RestResponse<Void> notExistProductExceptionHandler(NotExistProductException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NotExistTradeException.class)
    public RestResponse<Void> notExistTradeExceptionHandler(NotExistTradeException e) {
        return RestResponse.error(HttpStatus.BAD_REQUEST, e.getMessage() + Arrays.asList(e.getStackTrace()));
    }
}
