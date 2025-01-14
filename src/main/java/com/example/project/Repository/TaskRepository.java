package com.example.project.Repository;

import com.example.project.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour les tâches dans le contexte des projets.
 * Cette interface étend {@link JpaRepository} et fournit des méthodes
 * pour effectuer des opérations CRUD sur les entités {@link Task}.
 * Elle permet également de récupérer des tâches par ID de projet.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Récupérer toutes les tâches associées à un projet spécifique.
     *
     * @param projectId L'ID du projet pour lequel récupérer les tâches.
     * @return Une liste de tâches associées au projet spécifié.
     */
    List<Task> findByProjectId(Long projectId);

}
