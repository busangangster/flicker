package com.flicker.user.user.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/user")
public class UserController {


    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
