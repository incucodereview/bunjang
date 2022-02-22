package com.min.bunjang.join.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.join.dto.EmailJoinRequest;
import com.min.bunjang.join.service.EmailJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
public class EmailJoinController {
    private final EmailJoinService emailJoinService;

    @PostMapping(EmailJoinControllerPath.JOIN)
    public RestResponse<Void> join(
            @Validated @RequestBody EmailJoinRequest emailJoinRequest
    ) {
        emailJoinService.joinTempMember(emailJoinRequest);
        return RestResponse.of(HttpStatus.OK, null);
    }

    @PostMapping(EmailJoinControllerPath.CONFIRMED_MAIL)
    public RestResponse<Boolean> confirmedEmail(
            @NotBlank @RequestParam String token
    ) {
        emailJoinService.verifyConfirmEmailToken(token);
        return RestResponse.of(HttpStatus.OK, Boolean.FALSE);
    }
}
