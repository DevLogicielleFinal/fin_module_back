package com.example.project.Repository;
import com.example.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour les utilisateurs. Cette interface étend {@link JpaRepository} et fournit
 * des méthodes pour effectuer des opérations CRUD sur les entités {@link User}.
 * Elle permet également de rechercher des utilisateurs par email, nom d'utilisateur ou ID.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Trouver un utilisateur par son email.
     *
     * @param email L'email de l'utilisateur à rechercher.
     * @return Un {@link Optional} contenant l'utilisateur trouvé ou vide si aucun utilisateur n'a cet email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Trouver un utilisateur par son nom d'utilisateur.
     *
     * @param username Le nom d'utilisateur à rechercher.
     * @return Un {@link Optional} contenant l'utilisateur trouvé ou vide si aucun utilisateur n'a ce nom.
     */
    Optional<User> findByUsername(String username);

    /**
     * Trouver un utilisateur par son ID.
     *
     * @param id L'ID de l'utilisateur à rechercher.
     * @return Un {@link Optional} contenant l'utilisateur trouvé ou vide si aucun utilisateur n'a cet ID.
     */
    Optional<User> findById(Long id);

    /**
     * Récupérer une liste d'utilisateurs en fonction d'une liste d'IDs.
     *
     * @param ids Une liste d'IDs d'utilisateurs à rechercher.
     * @return Une liste d'utilisateurs correspondant aux IDs fournis.
     */
    List<User> findAllById(Iterable<Long> ids);
}
