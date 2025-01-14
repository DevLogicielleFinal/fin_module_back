package com.example.project.Controller;
import com.example.project.DTO.ProjectCreationDTO;
import com.example.project.DTO.ProjectDTO;
import com.example.project.DTO.UserDTO;
import com.example.project.Service.AuthenticationService;
import com.example.project.Service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Contrôleur de gestion des projets.
 * Fournit des routes pour créer un projet, récupérer les projets d'un utilisateur,
 * assigner un utilisateur à un projet et obtenir les utilisateurs d'un projet.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    /**
     * Service de gestion des projets.
     */
    private final ProjectService projectService;

    /**
     * Service d'authentification pour récupérer l'utilisateur authentifié.
     */
    private final AuthenticationService authenticationService;

    /**
     * Constructeur pour injecter les services nécessaires.
     *
     * @param projectService Le service de gestion des projets.
     * @param authenticationService Le service d'authentification pour récupérer l'utilisateur authentifié.
     */
    public ProjectController(ProjectService projectService, AuthenticationService authenticationService) {
        this.projectService = projectService;
        this.authenticationService = authenticationService;
    }

    /**
     * Route pour créer un projet.
     * Cette méthode crée un projet en utilisant les informations fournies dans le DTO de création de projet.
     * Le projet est lié à l'utilisateur actuellement authentifié.
     *
     * @param projectCreationDTO Les informations de création du projet (nom, description, etc.).
     * @return Une réponse HTTP indiquant le succès ou l'échec de la création du projet.
     */
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

    /**
     * Route pour récupérer les projets de l'utilisateur authentifié.
     * Cette méthode retourne la liste des projets auxquels l'utilisateur authentifié participe.
     *
     * @return Une liste de DTOs représentant les projets de l'utilisateur.
     */
    @GetMapping("/me")
    public List<ProjectDTO> getUserProjects() {
        Long userId = authenticationService.getAuthenticatedUserId();
        return projectService.getProjectsByUser(userId);
    }

    /**
     * Route pour assigner l'utilisateur authentifié à un projet.
     * Cette méthode assigne l'utilisateur authentifié au projet dont l'ID est spécifié.
     *
     * @param projectId L'ID du projet auquel l'utilisateur sera assigné.
     * @return Une réponse HTTP indiquant si l'assignation a réussi ou échoué.
     */
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

    /**
     * Route pour récupérer la liste des utilisateurs assignés à un projet.
     * Cette méthode retourne les utilisateurs qui sont membres d'un projet donné.
     *
     * @param projectId L'ID du projet pour lequel obtenir la liste des utilisateurs.
     * @return Une liste de DTOs représentant les utilisateurs assignés au projet.
     */
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
