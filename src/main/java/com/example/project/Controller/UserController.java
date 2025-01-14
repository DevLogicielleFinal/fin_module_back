package com.example.project.Controller;

import com.example.project.Entity.User;
import com.example.project.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Route pour créer un nouvel utilisateur
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
