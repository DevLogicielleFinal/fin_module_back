package com.example.project.Controller;
import com.example.project.DTO.ProjectCreationDTO;
import com.example.project.Entity.Project;
import com.example.project.Service.ProjectService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public Project createProject(@RequestBody ProjectCreationDTO projectCreationDTO) {
        // Récupérer l'ID utilisateur depuis le token JWT via SecurityContextHolder
        String userIdString = SecurityContextHolder.getContext().getAuthentication().getName();

        // Convertir l'ID utilisateur en Long (vérifiez si c'est possible)
        Long userId = Long.parseLong(userIdString);

        // Appeler la méthode du service pour créer le projet
        return projectService.createProject(userId, projectCreationDTO);
    }
}
