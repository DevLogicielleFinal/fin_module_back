package com.example.project.Service;

import com.example.project.Repository.UserRepository;
import com.example.project.Entity.User;
import com.example.project.Security.JWTUtil;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String authenticate(String email, String password) {
        // Chercher l'utilisateur par email
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Vérifier le mot de passe
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Générer un JWT
                return jwtUtil.generateToken(user.getId());
            } else {
                throw new IllegalArgumentException("Mot de passe incorrect");
            }
        } else {
            throw new IllegalArgumentException("Utilisateur non trouvé");
        }
    }

    // Méthode pour trouver un utilisateur par id
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur avec l'ID " + id + " non trouvé"));
    }

    public List<User> getUsersByIds(List<Long> userIds) {
        // Récupère tous les utilisateurs correspondant aux IDs
        return userRepository.findAllById(userIds);
    }
}
