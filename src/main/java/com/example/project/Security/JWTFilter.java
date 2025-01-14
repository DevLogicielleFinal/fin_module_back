package com.example.project.Security;
import jakarta.servlet.ServletException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Collections;
import java.util.List;

/**
 * Filtre de sécurité qui intercepte chaque requête HTTP pour extraire et valider un token JWT.
 * Si le token est valide, l'authentification est ajoutée au contexte de sécurité.
 * Ce filtre est exécuté une seule fois par requête.
 */
@Component
public class JWTFilter extends OncePerRequestFilter {

    /**
     * Utilitaire pour la gestion du JWT.
     */
    private final JWTUtil jwtUtil;

    /**
     * Constructeur pour injecter l'utilitaire de gestion du JWT.
     *
     * @param jwtUtil L'utilitaire JWT utilisé pour extraire et valider les tokens.
     */
    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Méthode principale de filtrage des requêtes HTTP.
     * Elle extrait le token JWT depuis l'en-tête de la requête, le valide, et si le token est valide,
     * elle ajoute l'authentification au contexte de sécurité de Spring.
     *
     * @param request  La requête HTTP.
     * @param response La réponse HTTP.
     * @param filterChain La chaîne de filtres de requêtes.
     * @throws ServletException Si une erreur survient lors du filtrage de la requête.
     * @throws IOException Si une erreur survient lors de l'exécution du filtre.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extraire le token du header Authorization
        String token = extractToken(request);

        if (token != null && jwtUtil.validateToken(token)) {
            // Si le token est valide, on crée l'authentification et on la place dans le SecurityContext
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    jwtUtil.extractUserId(token), null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrait le token JWT de l'en-tête Authorization de la requête HTTP.
     *
     * @param request La requête HTTP à analyser.
     * @return Le token JWT extrait, ou null si aucun token n'est trouvé.
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Retirer "Bearer " du début du token
        }
        return null;
    }
}
