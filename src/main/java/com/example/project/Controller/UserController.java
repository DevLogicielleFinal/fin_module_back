package com.example.project.Controller;

import com.example.project.Entity.User;
import com.example.project.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


/**
 * Contrôleur pour la gestion des utilisateurs.
 * Fournit des routes pour l'enregistrement d'un nouvel utilisateur.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    /**
     * Service de gestion des utilisateurs.
     */
    private final UserService userService;

    /**
     * Constructeur pour injecter le service de gestion des utilisateurs.
     *
     * @param userService Le service de gestion des utilisateurs.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Route pour enregistrer un nouvel utilisateur.
     * Cette méthode crée un nouvel utilisateur en utilisant les informations envoyées dans le corps de la requête.
     * Elle retourne un message de succès si l'enregistrement est réussi, ou un message d'erreur en cas d'échec.
     *
     * @param user L'utilisateur à enregistrer, dont les informations sont envoyées dans le corps de la requête.
     * @return Une réponse HTTP avec un message de succès ou d'erreur.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            // Appeler la méthode du service pour enregistrer l'utilisateur
            User savedUser = userService.saveUser(user);

            // Si l'utilisateur est correctement créé, renvoyer un message de succès
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
        } catch (Exception e) {
            // Si une exception survient (par exemple, un problème avec la base de données),
            // renvoyer un message d'erreur avec un code de statut approprié
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering the user.");
        }
    }
}
