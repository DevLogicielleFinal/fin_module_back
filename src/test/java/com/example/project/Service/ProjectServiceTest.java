package com.example.project.Service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.project.DTO.ProjectCreationDTO;
import com.example.project.DTO.ProjectDTO;
import com.example.project.DTO.UserDTO;
import com.example.project.Entity.Project;
import com.example.project.Entity.User;
import com.example.project.Repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createProject_ShouldReturnSavedProject() {
        // Préparation des données
        Long userId = 1L;
        User creator = new User();
        creator.setId(userId);
        creator.setUsername("John Doe");
        creator.setEmail("johndoe@example.com");

        ProjectCreationDTO projectCreationDTO = new ProjectCreationDTO();
        projectCreationDTO.setName("Test Project");
        projectCreationDTO.setDescription("This is a test project");

        Project project = new Project();
        project.setId(1L);
        project.setName(projectCreationDTO.getName());
        project.setDescription(projectCreationDTO.getDescription());
        project.setDateCreation(LocalDate.now());
        project.setState(Project.EtatProjet.TO_DO);
        project.setCreator(creator);

        when(userService.findById(userId)).thenReturn(creator);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Exécution
        Project createdProject = projectService.createProject(userId, projectCreationDTO);

        // Vérifications
        assertNotNull(createdProject);
        assertEquals("Test Project", createdProject.getName());
        assertEquals("This is a test project", createdProject.getDescription());
        assertEquals(Project.EtatProjet.TO_DO, createdProject.getState());
        assertEquals(userId, createdProject.getCreator().getId());

        verify(userService, times(1)).findById(userId);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    public void getProjectsByUser_ShouldReturnProjectDTOList() {
        // Préparation des données
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("John Doe");
        user.setEmail("johndoe@example.com");

        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Project 1");
        project1.setDescription("Description 1");
        project1.setDateCreation(LocalDate.now());
        project1.setState(Project.EtatProjet.TO_DO);
        project1.setCreator(user);

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Project 2");
        project2.setDescription("Description 2");
        project2.setDateCreation(LocalDate.now());
        project2.setState(Project.EtatProjet.EN_COURS);
        project2.setCreator(user);

        when(userService.findById(userId)).thenReturn(user);
        when(projectRepository.findByCreator(user)).thenReturn(Collections.singletonList(project1));
        when(projectRepository.findByMembersContains(user)).thenReturn(Collections.singletonList(project2));

        // Exécution
        List<ProjectDTO> projectDTOs = projectService.getProjectsByUser(userId);

        // Vérifications
        assertNotNull(projectDTOs);
        assertEquals(2, projectDTOs.size());
        assertEquals("Project 1", projectDTOs.get(0).getName());
        assertEquals("Project 2", projectDTOs.get(1).getName());

        verify(userService, times(1)).findById(userId);
        verify(projectRepository, times(1)).findByCreator(user);
        verify(projectRepository, times(1)).findByMembersContains(user);
    }

    @Test
    public void assignUserToProject_ShouldAddMemberToProject() {
        // Préparation des données
        Long projectId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);
        user.setUsername("Jane Doe");

        Project project = new Project();
        project.setId(projectId);
        project.setName("Project 1");
        project.setMembers(new HashSet<>());

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userService.findById(userId)).thenReturn(user);
        when(projectRepository.save(project)).thenReturn(project);

        // Exécution
        Project updatedProject = projectService.assignUserToProject(projectId, userId);

        // Vérifications
        assertNotNull(updatedProject);
        assertTrue(updatedProject.getMembers().contains(user));

        verify(projectRepository, times(1)).findById(projectId);
        verify(userService, times(1)).findById(userId);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    public void assignUserToProject_ShouldThrowExceptionWhenUserAlreadyAssigned() {
        // Préparation des données
        Long projectId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);
        user.setUsername("Jane Doe");

        Project project = new Project();
        project.setId(projectId);
        project.setName("Project 1");
        project.setMembers(new HashSet<>(Collections.singletonList(user)));

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userService.findById(userId)).thenReturn(user);

        // Exécution et vérification de l'exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> projectService.assignUserToProject(projectId, userId));

        assertEquals("User is already assigned to the project", exception.getMessage());

        verify(projectRepository, times(1)).findById(projectId);
        verify(userService, times(1)).findById(userId);
        verify(projectRepository, never()).save(any(Project.class));
    }
}