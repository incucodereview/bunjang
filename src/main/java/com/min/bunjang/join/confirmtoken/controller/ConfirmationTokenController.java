package com.min.bunjang.join.confirmtoken.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.join.confirmtoken.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ConfirmationTokenController {
    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping(ConfirmationTokenControllerPath.CONFIRMED_MAIL)
    public RestResponse<Boolean> confirmedEmail(
            @RequestParam String token
    ) {
        confirmationTokenService.verifyConfirmEmailToken(token);
    }

}
