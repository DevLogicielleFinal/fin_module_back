package com.example.project.Controller;

import com.example.project.DTO.TaskDto;
import com.example.project.Service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur de gestion des tâches.
 * Fournit des routes pour ajouter une tâche à un projet, modifier l'état d'une tâche,
 * récupérer les tâches d'un projet et assigner un utilisateur à une tâche.
 */
@RestController
@RequestMapping("/api")
public class TaskController {

    /**
     * Service de gestion des tâches.
     */
    private final TaskService taskService;

    /**
     * Constructeur pour injecter le service de gestion des tâches.
     *
     * @param taskService Le service de gestion des tâches.
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Route pour ajouter une tâche à un projet.
     * Cette méthode crée une tâche et l'associe au projet dont l'ID est spécifié dans l'URL.
     *
     * @param projectId L'ID du projet auquel la tâche sera ajoutée.
     * @param taskDto Les informations de la tâche à ajouter.
     * @return Une réponse HTTP avec un message de succès.
     */
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Map<String, String>> addTaskToProject(
            @PathVariable("projectId") Long projectId,
            @RequestBody TaskDto taskDto
    ) {
        taskService.addTaskToProject(projectId, taskDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Task added to project successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Route pour changer l'état d'une tâche.
     * Cette méthode modifie l'état de la tâche spécifiée par son ID avec l'état fourni dans le corps de la requête.
     *
     * @param taskId L'ID de la tâche dont l'état doit être modifié.
     * @param body Le corps de la requête contenant le nouvel état de la tâche (par exemple : { "newState": "DONE" }).
     * @return Une réponse HTTP avec un message indiquant que l'état de la tâche a été modifié avec succès.
     */
    @PutMapping("/tasks/{taskId}/state")
    public ResponseEntity<Map<String, String>> changeTaskState(
            @PathVariable("taskId") Long taskId,
            @RequestBody Map<String, String> body
    ) {
        String newState = body.get("newState");
        taskService.changeTaskState(taskId, newState);

        // Crée un map JSON pour la réponse
        Map<String, String> response = new HashMap<>();
        response.put("message", "Task state changed successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Route pour récupérer toutes les tâches d'un projet.
     * Cette méthode retourne la liste de toutes les tâches associées à un projet spécifié par son ID.
     * Seuls les utilisateurs ayant le rôle `ROLE_USER` ou `ROLE_ADMIN` peuvent accéder à cette route.
     *
     * @param projectId L'ID du projet dont les tâches doivent être récupérées.
     * @return Une réponse HTTP contenant une liste de DTOs représentant les tâches du projet.
     */
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasksByProject(
            @PathVariable("projectId") Long projectId
    ) {
        List<TaskDto> tasks = taskService.getAllTasksByProject(projectId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Route pour assigner un utilisateur à une tâche.
     * Cette méthode associe l'utilisateur spécifié à la tâche dont l'ID est fourni dans l'URL.
     * Seuls les utilisateurs ayant le rôle `ROLE_USER` ou `ROLE_ADMIN` peuvent accéder à cette route.
     *
     * @param taskId L'ID de la tâche à laquelle l'utilisateur sera assigné.
     * @param userId L'ID de l'utilisateur qui sera assigné à la tâche.
     * @return Une réponse HTTP avec un message de succès indiquant que l'utilisateur a été assigné à la tâche.
     */
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @PutMapping("/tasks/{taskId}/assign/{userId}")
    public ResponseEntity<Map<String, String>> assignUserToTask(
            @PathVariable("taskId") Long taskId,
            @PathVariable("userId") Long userId
    ) {
        taskService.assignUserToTask(taskId, userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Task assigned successfully");

        return ResponseEntity.ok(response);
    }
}
