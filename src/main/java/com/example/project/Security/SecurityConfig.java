package com.example.project.Security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration de la sécurité de l'application.
 * Cette classe configure les règles de sécurité pour l'application,
 * y compris la gestion des requêtes HTTP, les filtres de sécurité et
 * la gestion des sessions.
 * Elle permet aussi de configurer les accès aux différentes routes de l'application.
 */
@Configuration
public class SecurityConfig {
    private final JWTFilter jwtFilter;

    /**
     * Constructeur de la configuration de sécurité.
     *
     * @param jwtFilter Le filtre JWT personnalisé.
     */
    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Définir la chaîne de filtres de sécurité.
     * Cette méthode configure les règles de sécurité pour les requêtes HTTP.
     * Elle définit les règles d'autorisation, la gestion des sessions et l'ajout du filtre JWT.
     *
     * @param http L'objet HttpSecurity utilisé pour configurer la sécurité HTTP.
     * @return Le filtre de sécurité configuré.
     * @throws Exception Si une erreur survient lors de la configuration de la sécurité.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .disable()) // Désactiver CSRF pour toute l'application
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/users/register").permitAll() // Routes publiques sans authentification
                        .requestMatchers("/h2-console/**").permitAll() // Autoriser l'accès à la console H2
                        .anyRequest().authenticated() // Requiert une authentification pour toutes les autres routes
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Désactive les sessions
                );

        // Autoriser l'accès à la console H2, en permettant l'utilisation d'iframe
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        // Ajout du filtre JWT avant le filtre d'authentification par défaut
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Définir un encodeur de mot de passe.
     * Cette méthode définit un encodeur de mot de passe, utilisé pour sécuriser les mots de passe dans l'application.
     * Ici, on utilise BCrypt pour l'encodeur.
     *
     * @return L'encodeur de mot de passe.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Ici, on utilise BCrypt pour sécuriser les mots de passe
        return new BCryptPasswordEncoder();
    }
}
