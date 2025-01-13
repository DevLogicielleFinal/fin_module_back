package com.example.project.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws java.io.IOException, jakarta.servlet.ServletException {
        // Extraire le jeton JWT de l'en-tête Authorization
        String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String email = null;

        // Vérifier si l'en-tête contient un jeton et extraire l'email
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);  // Retirer le préfixe "Bearer "
            email = jwtUtil.extractUserId(token);  // Extraire l'email à partir du jeton
        }

        // Authentifier l'utilisateur si le jeton est valide
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(token)) {
                // Création de l'objet Authentication avec l'email et un rôle par défaut
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        email, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

                // Ajouter les détails de l'authentification
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Placer l'authentification dans le SecurityContext
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Passer la requête à la chaîne suivante (autres filtres ou contrôleurs)
        chain.doFilter(request, response);
    }
}
