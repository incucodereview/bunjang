package com.min.bunjang.join.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.join.confirmtoken.service.ConfirmationTokenService;
import com.min.bunjang.join.service.EmailJoinService;
import com.min.bunjang.member.dto.EmailJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailJoinController {
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailJoinService emailJoinService;

    @PostMapping(EmailJoinControllerPath.JOIN)
    public RestResponse<Void> join(
            @Validated @RequestBody EmailJoinRequest emailJoinRequest
    ) {
        emailJoinService.joinTempMember(emailJoinRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

//    @PostMapping(EmailJoinControllerPath.CONFIRMATION_EMAIL)
//    public RestResponse<Void> confirmEmail(
//            @Validated @RequestBody EmailConfirmRequest emailConfirmRequest
//    ) {
//        confirmationTokenService.sendConfirmEmailForJoin(emailConfirmRequest);
//    }
}
