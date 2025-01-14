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

/**
 * Service pour la gestion des projets.
 * Cette classe contient la logique métier pour la création, l'attribution d'utilisateurs aux projets,
 * la récupération des projets d'un utilisateur, ainsi que la gestion des membres d'un projet.
 */
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    /**
     * Constructeur de la classe ProjectService.
     *
     * @param projectRepository Le repository pour accéder aux données des projets.
     * @param userService Le service pour accéder aux données des utilisateurs.
     */
    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    /**
     * Créer un nouveau projet et l'enregistrer dans la base de données.
     * Cette méthode crée un projet à partir des données fournies et l'associe à l'utilisateur qui le crée.
     * Le créateur du projet est ajouté comme membre du projet.
     *
     * @param userId L'ID de l'utilisateur qui crée le projet.
     * @param projectCreationDTO Les informations du projet à créer.
     * @return Le projet créé.
     */
    public Project createProject(Long userId, ProjectCreationDTO projectCreationDTO) {
        User creator = userService.findById(userId);

        Project project = new Project();
        project.setName(projectCreationDTO.getName());
        project.setDescription(projectCreationDTO.getDescription());
        project.setDateCreation(LocalDate.now());
        project.setState(Project.EtatProjet.TO_DO);
        project.setCreator(creator);

        // Ajouter le créateur comme membre du projet
        project.addMember(creator);

        // Enregistrer le projet dans la base de données
        return projectRepository.save(project);
    }

    /**
     * Récupérer tous les projets d'un utilisateur.
     * Cette méthode retourne la liste de tous les projets associés à un utilisateur,
     * qu'il en soit le créateur ou un membre.
     *
     * @param userId L'ID de l'utilisateur dont on veut récupérer les projets.
     * @return La liste des projets associés à l'utilisateur.
     */
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

    /**
     * Assigner un utilisateur à un projet.
     * Cette méthode ajoute un utilisateur au projet spécifié. Si l'utilisateur est déjà membre,
     * une exception est levée. Le processus d'ajout est sécurisé par un verrou pour éviter les conflits
     * en cas de modification simultanée des membres du projet.
     *
     * @param projectId L'ID du projet auquel l'utilisateur doit être assigné.
     * @param userId L'ID de l'utilisateur à assigner.
     * @return Le projet mis à jour.
     * @throws IllegalArgumentException Si le projet ou l'utilisateur est introuvable ou si l'utilisateur est déjà membre du projet.
     */
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

    /**
     * Récupérer tous les utilisateurs d'un projet.
     * Cette méthode retourne tous les membres du projet spécifié sous forme de liste de UserDTO.
     *
     * @param projectId L'ID du projet pour lequel récupérer les utilisateurs.
     * @return La liste des utilisateurs associés au projet.
     * @throws IllegalArgumentException Si le projet n'est pas trouvé.
     */
    public List<UserDTO> getUsersByProject(Long projectId) {
        // Récupérer le projet
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // Mapper les membres en UserDTO
        List<UserDTO> members = project.getMembers().stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());

        return members; // Retourne la liste des utilisateurs
    }
}
