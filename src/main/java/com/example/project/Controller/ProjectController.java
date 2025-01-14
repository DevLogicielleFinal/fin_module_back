package com.example.project.Controller;
import com.example.project.DTO.ProjectCreationDTO;
import com.example.project.DTO.ProjectDTO;
import com.example.project.DTO.UserDTO;
import com.example.project.Entity.Project;
import com.example.project.Service.AuthenticationService;
import com.example.project.Service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final AuthenticationService authenticationService;

    public ProjectController(ProjectService projectService, AuthenticationService authenticationService) {
        this.projectService = projectService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<String> createProject(@RequestBody ProjectCreationDTO projectCreationDTO) {
        try {
            Long userId = authenticationService.getAuthenticatedUserId();

            // Appeler la méthode du service pour créer le projet
            projectService.createProject(userId, projectCreationDTO);

            // Retourner une réponse de succès
            return ResponseEntity.status(HttpStatus.CREATED).body("Project created successfully.");
        } catch (Exception e) {
            // Retourner une réponse d'erreur générique
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the project.");
        }
    }

    // Nouvelle route pour récupérer les projets de l'utilisateur authentifié
    @GetMapping("/me")
    public List<ProjectDTO> getUserProjects() {
        Long userId = authenticationService.getAuthenticatedUserId();
        return projectService.getProjectsByUser(userId);
    }

    @PostMapping("/{projectId}/assign")
    public ResponseEntity<String> assignAuthenticatedUserToProject(@PathVariable Long projectId) {
        try {
            // Récupérer l'ID de l'utilisateur authentifié
            Long authenticatedUserId = authenticationService.getAuthenticatedUserId();

            // Appeler le service pour assigner l'utilisateur au projet
            projectService.assignUserToProject(projectId, authenticatedUserId);

            // Retourner une réponse de succès
            return ResponseEntity.ok("User successfully assigned to the project.");
        } catch (IllegalArgumentException e) {
            // Retourner une réponse d'erreur si le projet n'est pas trouvé ou si l'utilisateur est déjà assigné
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Retourner une réponse d'erreur générique en cas d'autres problèmes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while assigning the user to the project.");
        }
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<UserDTO>> getUsersByProject(@PathVariable Long projectId) {
        try {
            List<UserDTO> users = projectService.getUsersByProject(projectId);
            return ResponseEntity.ok(users); // Renvoie un statut HTTP 200 avec la liste des utilisateurs
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Si le projet n'existe pas
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // En cas d'erreur serveur
        }
    }
}
