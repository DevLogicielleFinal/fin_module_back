package com.example.project.Service;
import com.example.project.DTO.ProjectCreationDTO;
import com.example.project.Entity.Project;
import com.example.project.Entity.User;
import com.example.project.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // Récupérer les projets de l'utilisateur (en tant que créateur ou membre)
    public List<Project> getProjectsByUser(Long userId) {
        User user = userService.findById(userId);

        // Trouver les projets dont l'utilisateur est le créateur ou un membre
        List<Project> projectsCreatedByUser = projectRepository.findByCreator(user);  // Projets où l'utilisateur est créateur
        List<Project> projectsUserIsMemberOf = projectRepository.findByMembersContains(user);  // Projets où l'utilisateur est membre

        // Combiner les deux listes (en évitant les doublons)
        Set<Project> allProjects = new HashSet<>();
        allProjects.addAll(projectsCreatedByUser);
        allProjects.addAll(projectsUserIsMemberOf);

        return new ArrayList<>(allProjects);  // Retourner les projets sous forme de liste
    }
}
