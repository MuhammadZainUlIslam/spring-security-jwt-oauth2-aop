package com.example.detailed_authentication.dto;

import com.example.detailed_authentication.entity.role.UserRole;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Setter
@Getter
public class RegistrationRequestDto {

    private String username;
    private String email;
    private String password;
    private UserRole userRole;
}
