package com.example.project.Service;

import com.example.project.Security.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class AuthenticationService {
    private final JWTUtil jwtUtil;

    public AuthenticationService(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Extraire l'ID de l'utilisateur à partir du token JWT
    public Long getAuthenticatedUserId() {
        String userIdString = SecurityContextHolder.getContext().getAuthentication().getName();

        // Convertir l'ID utilisateur en Long (vérifiez si c'est possible)
        Long userId = Long.parseLong(userIdString);

        return userId;
    }
}
