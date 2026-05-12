package com.example.detailed_authentication.service;

import com.example.detailed_authentication.dto.AuthResponseDto;
import com.example.detailed_authentication.dto.LoginRequestDto;
import com.example.detailed_authentication.dto.RegistrationRequestDto;
import com.example.detailed_authentication.dto.RegistrationResponseDto;
import com.example.detailed_authentication.entity.User;
import com.example.detailed_authentication.entity.role.AuthProvider;
import com.example.detailed_authentication.entity.role.UserRole;
import com.example.detailed_authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public RegistrationResponseDto registerLocalUser(RegistrationRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserRole(
                request.getUserRole() != null ? request.getUserRole() : UserRole.USER
        );
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setEnabled(true);

        userRepository.save(user);

        return new RegistrationResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                "Local user registered successfully"
        );
    }

    public AuthResponseDto login(LoginRequestDto request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDto(token);
    }

}
