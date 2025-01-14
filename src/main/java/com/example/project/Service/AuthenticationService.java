package com.example.project.Service;

import com.example.project.Security.JWTUtil;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Service pour la gestion de l'authentification des utilisateurs.
 * Cette classe permet d'extraire l'ID de l'utilisateur authentifié à partir du contexte de sécurité,
 * en utilisant le jeton JWT stocké dans le `SecurityContext`.
 */
@Service
public class AuthenticationService {
    private final JWTUtil jwtUtil;

    /**
     * Constructeur de la classe AuthenticationService.
     *
     * @param jwtUtil Utilitaire pour la gestion des JWT, utilisé pour la création et la validation des tokens.
     */
    public AuthenticationService(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Extraire l'ID de l'utilisateur authentifié à partir du token JWT dans le SecurityContext.
     *
     * Cette méthode accède au contexte de sécurité pour récupérer le nom de l'utilisateur actuellement authentifié.
     * Elle suppose que le nom de l'utilisateur est l'ID de l'utilisateur sous forme de chaîne de caractères.
     * Ensuite, l'ID est converti en un type Long et retourné.
     *
     * @return L'ID de l'utilisateur authentifié sous forme de Long.
     * @throws NumberFormatException Si le nom de l'utilisateur dans le SecurityContext ne peut pas être converti en Long.
     */
    public Long getAuthenticatedUserId() {
        String userIdString = SecurityContextHolder.getContext().getAuthentication().getName();

        // Convertir l'ID utilisateur en Long (vérifiez si c'est possible)
        Long userId = Long.parseLong(userIdString);

        return userId;
    }
}
