package com.example.detailed_authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegistrationResponseDto {

    private Long id;
    private String username;
    private String email;
    private String message;
}
