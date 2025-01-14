package com.example.project.Controller;
import com.example.project.DTO.LoginRequest;
import com.example.project.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur de gestion de l'authentification des utilisateurs.
 * Fournit des routes pour se connecter à l'application via un email et un mot de passe.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    /**
     * Service utilisateur utilisé pour l'authentification.
     */
    private final UserService userService;

    /**
     * Constructeur pour injecter le service utilisateur.
     *
     * @param userService Le service utilisateur à injecter.
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Route de login pour authentifier un utilisateur et générer un JWT.
     * L'utilisateur fournit un email et un mot de passe, et si les informations sont correctes,
     * un JWT est retourné pour permettre une authentification ultérieure.
     *
     * @param loginRequest L'objet contenant l'email et le mot de passe de l'utilisateur.
     * @return Une réponse HTTP avec le JWT généré, si l'authentification réussie.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String jwt = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(jwt);
    }
}
