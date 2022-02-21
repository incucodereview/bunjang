package com.min.bunjang.member.controller;

import com.min.bunjang.common.dto.RestResponse;
import com.min.bunjang.member.dto.EmailJoinRequest;
import com.min.bunjang.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

}
