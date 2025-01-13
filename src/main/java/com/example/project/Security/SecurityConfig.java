package com.example.project.Security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {
    private final JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Ici, on utilise BCrypt pour sécuriser les mots de passe
        return new BCryptPasswordEncoder();
    }
}
