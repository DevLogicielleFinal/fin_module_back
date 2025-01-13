package com.example.project.Controller;
import com.example.project.DTO.ProjectCreationDTO;
import com.example.project.Entity.Project;
import com.example.project.Service.AuthenticationService;
import com.example.project.Service.ProjectService;
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
    public Project createProject(@RequestBody ProjectCreationDTO projectCreationDTO) {
        Long userId = authenticationService.getAuthenticatedUserId();

        // Appeler la méthode du service pour créer le projet
        return projectService.createProject(userId, projectCreationDTO);
    }

    // Nouvelle route pour récupérer les projets de l'utilisateur authentifié
    @GetMapping("/me")
    public List<Project> getUserProjects() {
        Long userId = authenticationService.getAuthenticatedUserId();
        return projectService.getProjectsByUser(userId);
    }
}
