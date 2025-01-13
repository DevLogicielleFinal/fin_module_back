package com.example.project;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                return jwtUtil.generateToken(user.getEmail());
            } else {
                throw new IllegalArgumentException("Mot de passe incorrect");
            }
        } else {
            throw new IllegalArgumentException("Utilisateur non trouvé");
        }
    }
}
