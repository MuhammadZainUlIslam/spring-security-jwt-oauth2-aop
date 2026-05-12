package com.example.detailed_authentication.controller;

import com.example.detailed_authentication.dto.AuthResponseDto;
import com.example.detailed_authentication.dto.LoginRequestDto;
import com.example.detailed_authentication.dto.RegistrationRequestDto;
import com.example.detailed_authentication.dto.RegistrationResponseDto;
import com.example.detailed_authentication.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegistrationResponseDto registerLocal(
            @RequestBody RegistrationRequestDto request
    ) {
        return authService.registerLocalUser(request);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }

}
