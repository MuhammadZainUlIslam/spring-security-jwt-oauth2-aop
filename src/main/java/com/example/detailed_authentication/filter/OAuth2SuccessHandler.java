package com.example.detailed_authentication.filter;

import com.example.detailed_authentication.dto.AuthResponseDto;
import com.example.detailed_authentication.entity.User;
import com.example.detailed_authentication.service.CustomUserDetailsService;
import com.example.detailed_authentication.service.JwtService;
import com.example.detailed_authentication.service.OAuth2Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2Service oAuth2Service;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = ((OAuth2AuthenticationToken) authentication)
                .getAuthorizedClientRegistrationId();

        User user;

        if ("google".equalsIgnoreCase(registrationId)) {

            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");

            user = oAuth2Service.registerOrGetGoogleUser(email, name);

        } else {

            String username = oAuth2User.getAttribute("login");
            String email = oAuth2User.getAttribute("email");

            user = oAuth2Service.registerOrGetGithubUser(username, email);
        }

        String identifier = user.getEmail() != null
                ? user.getEmail()
                : user.getUsername();

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(identifier);

        String token = jwtService.generateToken(userDetails);
        AuthResponseDto authResponse = new AuthResponseDto(token);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(new ObjectMapper().writeValueAsString(authResponse));
        response.getWriter().flush();
    }
}
