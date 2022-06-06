package com.min.bunjang.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginTestController {

    @GetMapping("/login")
    public String loginTest() {
        return "/LoginTest";
    }

}
