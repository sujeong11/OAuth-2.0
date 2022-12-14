package com.sujeong.oauth20.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "개인 정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/content")
    public @ResponseBody String content() {
        return "게시판 내용";
    }
}