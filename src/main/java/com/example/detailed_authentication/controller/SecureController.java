package com.example.detailed_authentication.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure")
public class SecureController {

    @GetMapping("/hello")
    public String securedEndpoint() {
        return "You accessed secured API successfully!";
    }
}
