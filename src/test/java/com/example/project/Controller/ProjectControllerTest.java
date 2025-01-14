package com.example.project.Controller;

import com.example.project.DTO.ProjectCreationDTO;
import com.example.project.DTO.ProjectDTO;
import com.example.project.Service.AuthenticationService;
import com.example.project.Service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProjectService projectService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    /*@Test
    public void createProject_WithValidData_ShouldReturnSavedProject() throws Exception {
        // Préparation des données
        Long userId = 1L;
        ProjectCreationDTO projectCreationDTO = new ProjectCreationDTO("Test Project", "Description");
        String requestBody = """
        {
            "name": "Test Project",
            "description": "Description"
        }
        """;

        when(authenticationService.getAuthenticatedUserId()).thenReturn(userId);
        when(projectService.createProject(eq(userId), any(ProjectCreationDTO.class)))
                .thenReturn(new ProjectDTO(1L, "Test Project", "Description", LocalDate.now(), "TO_DO", null, null));

        // Exécution et vérifications
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Project"))
                .andExpect(jsonPath("$.description").value("Description"));

        verify(authenticationService, times(1)).getAuthenticatedUserId();
        verify(projectService, times(1)).createProject(eq(userId), any(ProjectCreationDTO.class));
    }*/

    @Test
    public void getUserProjects_ShouldReturnProjectsList() throws Exception {
        // Préparation des données
        Long userId = 1L;
        List<ProjectDTO> projects = Arrays.asList(
                new ProjectDTO(1L, "Project 1", "Description 1", LocalDate.now(), "TO_DO", null, null),
                new ProjectDTO(2L, "Project 2", "Description 2", LocalDate.now(), "IN_PROGRESS", null, null)
        );

        when(authenticationService.getAuthenticatedUserId()).thenReturn(userId);
        when(projectService.getProjectsByUser(userId)).thenReturn(projects);

        // Exécution et vérifications
        mockMvc.perform(get("/api/projects/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Project 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Project 2"));

        verify(authenticationService, times(1)).getAuthenticatedUserId();
        verify(projectService, times(1)).getProjectsByUser(userId);
    }

    @Test
    public void assignAuthenticatedUserToProject_ShouldReturnSuccessMessage() throws Exception {
        // Préparation des données
        Long projectId = 1L;
        Long authenticatedUserId = 1L;

        // Mock des dépendances
        when(authenticationService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(projectService.assignUserToProject(projectId, authenticatedUserId)).thenReturn(null);

        // Exécution et vérifications
        mockMvc.perform(post("/api/projects/{projectId}/assign", projectId))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully assigned to the project."));

        // Vérification des appels
        verify(authenticationService, times(1)).getAuthenticatedUserId();
        verify(projectService, times(1)).assignUserToProject(projectId, authenticatedUserId);
    }

    @Test
    public void assignAuthenticatedUserToProject_ShouldReturnBadRequestOnIllegalArgument() throws Exception {
        // Préparation des données
        Long projectId = 1L;
        Long authenticatedUserId = 1L;
        String errorMessage = "User is already assigned to the project";

        // Mock des dépendances
        when(authenticationService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        doThrow(new IllegalArgumentException(errorMessage))
                .when(projectService).assignUserToProject(projectId, authenticatedUserId);

        // Exécution et vérifications
        mockMvc.perform(post("/api/projects/{projectId}/assign", projectId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        // Vérification des appels
        verify(authenticationService, times(1)).getAuthenticatedUserId();
        verify(projectService, times(1)).assignUserToProject(projectId, authenticatedUserId);
    }

    @Test
    public void assignAuthenticatedUserToProject_ShouldReturnInternalServerErrorOnGenericException() throws Exception {
        // Préparation des données
        Long projectId = 1L;
        Long authenticatedUserId = 1L;

        // Mock des dépendances
        when(authenticationService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        doThrow(new RuntimeException("Unexpected error"))
                .when(projectService).assignUserToProject(projectId, authenticatedUserId);

        // Exécution et vérifications
        mockMvc.perform(post("/api/projects/{projectId}/assign", projectId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred while assigning the user to the project."));

        // Vérification des appels
        verify(authenticationService, times(1)).getAuthenticatedUserId();
        verify(projectService, times(1)).assignUserToProject(projectId, authenticatedUserId);
    }

    @Test
    public void assignAuthenticatedUserToProject_AlreadyAssigned_ShouldReturnBadRequest() throws Exception {
        // Préparation des données
        Long projectId = 1L;
        Long authenticatedUserId = 1L;

        when(authenticationService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        doThrow(new IllegalArgumentException("User is already assigned to the project"))
                .when(projectService).assignUserToProject(projectId, authenticatedUserId);

        // Exécution et vérifications
        mockMvc.perform(post("/api/projects/{projectId}/assign", projectId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User is already assigned to the project"));

        verify(authenticationService, times(1)).getAuthenticatedUserId();
        verify(projectService, times(1)).assignUserToProject(projectId, authenticatedUserId);
    }
}