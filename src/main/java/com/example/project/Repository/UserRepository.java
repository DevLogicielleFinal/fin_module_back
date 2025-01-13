package com.example.project.Repository;
import com.example.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Vous pouvez ajouter des requêtes personnalisées ici
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    List<User> findAllById(Iterable<Long> ids);
}
