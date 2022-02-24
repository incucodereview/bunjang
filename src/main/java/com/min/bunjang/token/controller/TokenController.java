package com.min.bunjang.token.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.token.dto.TokenValidResponse;
import com.min.bunjang.token.dto.TokenValuesDto;
import com.min.bunjang.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;
    
    @PostMapping(TokenControllerPath.VALIDATE_TOKEN_STATUS)
    public RestResponse<TokenValidResponse> validateTokenStatus(@Validated @RequestBody TokenValuesDto tokenValuesDto) {
        return RestResponse.of(HttpStatus.OK, tokenService.handleTokenOnTokenStatus(tokenValuesDto));
    }
}
