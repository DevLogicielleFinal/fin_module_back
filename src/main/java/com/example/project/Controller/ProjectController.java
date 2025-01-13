package com.example.project.Controller;
import com.example.project.DTO.ProjectCreationDTO;
import com.example.project.Entity.Project;
import com.example.project.Service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projetService;

    public ProjectController(ProjectService projetService) {
        this.projetService = projetService;
    }

    @GetMapping
    public List<Project> getProjetsUtilisateur(Authentication authentication) {
        // Récupère le nom d'utilisateur depuis le token (Spring Security)
        String username = authentication.getName();
        return projetService.getProjetsByUtilisateur(username);
    }

    @PostMapping
    public Project creerProjet(@Valid @RequestBody ProjectCreationDTO projetDTO, Authentication authentication) {
        String username = authentication.getName(); // Récupère l'utilisateur connecté
        return projetService.creerProjet(username, projetDTO);
    }
}
