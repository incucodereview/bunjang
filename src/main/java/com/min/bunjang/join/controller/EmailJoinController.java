package com.min.bunjang.join.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.join.dto.JoinRequest;
import com.min.bunjang.join.dto.TempJoinRequest;
import com.min.bunjang.join.service.EmailJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
public class EmailJoinController {
    private final EmailJoinService emailJoinService;

    @PostMapping(EmailJoinControllerPath.JOIN_TEMP_MEMBER_REQUEST)
    public RestResponse<Void> joinTempMember(@Validated @RequestBody TempJoinRequest tempJoinRequest) {
        emailJoinService.joinTempMember(tempJoinRequest);
        return RestResponse.of(HttpStatus.CREATED, null);
    }

    @GetMapping(EmailJoinControllerPath.CONFIRM_EMAIL_REQUEST)
    public RestResponse<String> confirmEmail(@NotBlank @RequestParam("token") String token) {
        String email = emailJoinService.verifyConfirmEmailToken(token);
        return RestResponse.of(HttpStatus.OK, email);
    }

    @PostMapping(EmailJoinControllerPath.JOIN_MEMBER_REQUEST)
    public RestResponse<Void> joinMember(@Validated @RequestBody JoinRequest joinRequest) {
        emailJoinService.joinMember(joinRequest.getEmail());
        return RestResponse.of(HttpStatus.CREATED, null);
    }
}
