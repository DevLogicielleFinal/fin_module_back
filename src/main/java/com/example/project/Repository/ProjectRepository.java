package com.example.project.Repository;

import com.example.project.Entity.Project;
import com.example.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository pour les projets. Cette interface étend {@link JpaRepository} et fournit
 * des méthodes pour effectuer des opérations CRUD sur les entités {@link Project}.
 * Elle permet également de rechercher des projets en fonction de leur créateur ou des membres associés.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
    /**
     * Trouver les projets créés par un utilisateur.
     *
     * @param creator L'utilisateur créateur des projets à rechercher.
     * @return Une liste de projets créés par l'utilisateur spécifié.
     */
    List<Project> findByCreator(User creator);

    /**
     * Trouver les projets auxquels un utilisateur est membre.
     *
     * @param member L'utilisateur membre des projets à rechercher.
     * @return Une liste de projets auxquels l'utilisateur spécifié est membre.
     */
    List<Project> findByMembersContains(User member);
}
