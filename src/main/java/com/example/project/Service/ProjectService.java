package com.example.project.Service;
import com.example.project.DTO.ProjectCreationDTO;
import com.example.project.DTO.ProjectDTO;
import com.example.project.DTO.UserDTO;
import com.example.project.Entity.Project;
import com.example.project.Entity.User;
import com.example.project.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<ProjectDTO> getProjectsByUser(Long userId) {
        User user = userService.findById(userId);

        // Trouver les projets dont l'utilisateur est le créateur ou un membre
        List<Project> projectsCreatedByUser = projectRepository.findByCreator(user);  // Projets où l'utilisateur est créateur
        List<Project> projectsUserIsMemberOf = projectRepository.findByMembersContains(user);  // Projets où l'utilisateur est membre

        // Combiner les deux listes (en évitant les doublons)
        Set<Project> allProjects = new HashSet<>();
        allProjects.addAll(projectsCreatedByUser);
        allProjects.addAll(projectsUserIsMemberOf);

        // Mapper les entités en DTOs
        List<ProjectDTO> projectDTOs = allProjects.stream().map(project -> {
            // Mapper le créateur et les membres du projet
            UserDTO creatorDTO = new UserDTO(project.getCreator().getId(), project.getCreator().getUsername(), project.getCreator().getEmail());
            List<UserDTO> membersDTO = project.getMembers().stream()
                    .map(member -> new UserDTO(member.getId(), member.getUsername(), member.getEmail()))
                    .collect(Collectors.toList());

            // Retourner le DTO du projet
            return new ProjectDTO(
                    project.getId(),
                    project.getName(),
                    project.getDescription(),
                    project.getDateCreation(),
                    project.getState().toString(),
                    creatorDTO,
                    membersDTO
            );
        }).collect(Collectors.toList());

        return projectDTOs;
        //return new ArrayList<>(allProjects);
    }

    public Project assignUserToProject(Long projectId, Long userId) {
        // Récupérer le projet
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // Récupérer l'utilisateur via le service
        User user = userService.findById(userId);

        // Vérifier si l'utilisateur est déjà assigné au projet
        if (project.getMembers().contains(user)) {
            throw new IllegalArgumentException("User is already assigned to the project");
        }

        // Ajouter l'utilisateur au projet
        project.addMember(user);

        // Sauvegarder les modifications
        return projectRepository.save(project);
    }

    public List<UserDTO> getUsersByProject(Long projectId) {
        // Récupérer le projet
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // Mapper les membres en UserDTO
        List<UserDTO> members = project.getMembers().stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());

        // Ajouter le créateur comme utilisateur du projet
        User creator = project.getCreator();
        members.add(new UserDTO(creator.getId(), creator.getUsername(), creator.getEmail()));

        return members; // Retourne la liste des utilisateurs
    }
}
