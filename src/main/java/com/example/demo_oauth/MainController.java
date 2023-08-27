package com.example.demo_oauth;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class MainController {
    @GetMapping
    public String root(){
        return "root!";
    }

    @GetMapping("/api/test")
    public String api(){
        return "/api/test!";
    }
}
