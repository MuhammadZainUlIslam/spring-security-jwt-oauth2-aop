package com.example.detailed_authentication.service;

import com.example.detailed_authentication.entity.User;
import com.example.detailed_authentication.entity.role.AuthProvider;
import com.example.detailed_authentication.entity.role.UserRole;
import com.example.detailed_authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final UserRepository userRepository;

    public User registerOrGetGoogleUser(String email, String name) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        return  existingUser.orElseGet(() -> {
            User user = new User();
            user.setEmail(email);
            user.setUsername(name);
            user.setAuthProvider(AuthProvider.GOOGLE);
            user.setUserRole(UserRole.USER);
            user.setEnabled(true);
            return userRepository.save(user);
        });
    }

    public User registerOrGetGithubUser(String username, String email) {
        Optional<User> existingUser = userRepository.findByUsername(username);

        return existingUser.orElseGet(() -> {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setAuthProvider(AuthProvider.GITHUB);
            user.setUserRole(UserRole.USER);
            user.setEnabled(true);
            return userRepository.save(user);
        });
    }
}
