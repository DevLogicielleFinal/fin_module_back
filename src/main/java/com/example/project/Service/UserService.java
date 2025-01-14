package com.example.project.Service;

import com.example.project.Repository.UserRepository;
import com.example.project.Entity.User;
import com.example.project.Security.JWTUtil;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des utilisateurs.
 * Cette classe contient les méthodes de gestion des utilisateurs, y compris l'enregistrement,
 * l'authentification, la recherche d'un utilisateur par son ID et la récupération des utilisateurs par une liste d'IDs.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    /**
     * Constructeur de la classe UserService.
     *
     * @param userRepository Le repository pour accéder aux utilisateurs dans la base de données.
     * @param passwordEncoder L'encodeur pour sécuriser les mots de passe des utilisateurs.
     * @param jwtUtil L'utilitaire pour générer et valider les tokens JWT.
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Enregistrer un nouvel utilisateur dans la base de données.
     * Cette méthode encode le mot de passe de l'utilisateur avant de l'enregistrer.
     *
     * @param user L'utilisateur à enregistrer.
     * @return L'utilisateur enregistré avec le mot de passe encodé.
     */
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Authentifier un utilisateur en fonction de son email et mot de passe.
     * Si l'authentification est réussie, un token JWT est généré et retourné.
     *
     * @param email L'email de l'utilisateur pour l'authentification.
     * @param password Le mot de passe de l'utilisateur pour l'authentification.
     * @return Le token JWT de l'utilisateur si l'authentification réussit.
     * @throws IllegalArgumentException Si l'utilisateur n'est pas trouvé ou si le mot de passe est incorrect.
     */
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

    /**
     * Trouver un utilisateur par son ID.
     *
     * @param id L'ID de l'utilisateur à rechercher.
     * @return L'utilisateur trouvé.
     * @throws RuntimeException Si l'utilisateur avec l'ID spécifié n'est pas trouvé.
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur avec l'ID " + id + " non trouvé"));
    }

    /**
     * Récupérer une liste d'utilisateurs par leurs IDs.
     *
     * @param userIds La liste des IDs des utilisateurs à récupérer.
     * @return La liste des utilisateurs trouvés.
     */
    public List<User> getUsersByIds(List<Long> userIds) {
        // Récupère tous les utilisateurs correspondant aux IDs
        return userRepository.findAllById(userIds);
    }
}
