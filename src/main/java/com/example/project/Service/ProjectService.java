package com.example.project.Service;
import com.example.project.DTO.ProjectCreationDTO;
import com.example.project.Entity.Project;
import com.example.project.Entity.User;
import com.example.project.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    public Project createProject(Long userId, ProjectCreationDTO projectCreationDTO) {
        // Récupérer l'utilisateur à partir de son ID
        User creator = userService.findById(userId);

        // Mapper le DTO vers l'entité Project
        Project project = new Project();
        project.setName(projectCreationDTO.getName());
        project.setDescription(projectCreationDTO.getDescription());
        project.setDateCreation(LocalDate.now());  // Date de création actuelle
        project.setState(Project.EtatProjet.TO_DO); // Etat initial (par défaut)

        // Assigner le créateur du projet
        project.setCreator(creator);

        // Enregistrer le projet dans la base de données
        return projectRepository.save(project);
    }
}
