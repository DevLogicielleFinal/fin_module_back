package com.example.project.Service;

import com.example.project.DTO.ProjectCreationDTO;
import com.example.project.DTO.ProjectDTO;
import com.example.project.DTO.UserDTO;
import com.example.project.Entity.Project;
import com.example.project.Entity.User;
import com.example.project.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        User creator = userService.findById(userId);

        Project project = new Project();
        project.setName(projectCreationDTO.getName());
        project.setDescription(projectCreationDTO.getDescription());
        project.setDateCreation(LocalDate.now());
        project.setState(Project.EtatProjet.TO_DO);
        project.setCreator(creator);

        return projectRepository.save(project);
    }

    public List<ProjectDTO> getProjectsByUser(Long userId) {
        User user = userService.findById(userId);

        List<Project> projectsCreatedByUser = projectRepository.findByCreator(user);
        List<Project> projectsUserIsMemberOf = projectRepository.findByMembersContains(user);

        Set<Project> allProjects = new HashSet<>();
        allProjects.addAll(projectsCreatedByUser);
        allProjects.addAll(projectsUserIsMemberOf);

        return allProjects.stream().map(project -> {
            UserDTO creatorDTO = new UserDTO(project.getCreator().getId(), project.getCreator().getUsername(), project.getCreator().getEmail());
            List<UserDTO> membersDTO = project.getMembers().stream()
                    .map(member -> new UserDTO(member.getId(), member.getUsername(), member.getEmail()))
                    .collect(Collectors.toList());

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
    }

    public Project assignUserToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        User user = userService.findById(userId);

        // Utilisation du verrou pour gérer la concurrence
        try {
            project.lockProject();

            if (project.getMembers().contains(user)) {
                throw new IllegalArgumentException("User is already assigned to the project");
            }

            project.addMember(user);
            return projectRepository.save(project);
        } finally {
            project.unlockProject();
        }
    }
}