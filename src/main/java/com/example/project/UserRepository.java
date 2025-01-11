package com.example.project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Vous pouvez ajouter des requêtes personnalisées ici
    Optional<User> findByEmail(String email);
}
