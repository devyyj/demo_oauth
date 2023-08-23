package com.example.demo_oauth;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class MainController {
    @GetMapping
    public String root(Authentication authentication){
        return "root!";
    }

    @GetMapping("/api/test")
    public String api(Authentication authentication){
        return "/api/test!";
    }
}
