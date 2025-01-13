package com.example.project.Controller;

import com.example.project.Entity.User;
import com.example.project.Service.UserService;
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
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/protected")
    public String testProtectedRoute(@RequestHeader("Authorization") String token) {
        return "Accès autorisé. Token valide : " + token;
    }
}
