package com.example.project.Controller;

import com.example.project.DTO.TaskDto;
import com.example.project.Service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test pour TaskController
 */
@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Construction d'un MockMvc standalone pour le controller
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    // ----------------------------------------------------------
    // 1) Test: addTaskToProject (POST /api/projects/{projectId}/tasks)
    // ----------------------------------------------------------
    @Test
    @DisplayName("POST /api/projects/{projectId}/tasks -> 200 OK et message de succès")
    void testAddTaskToProject() throws Exception {
        Long projectId = 1L;

        // Préparation du JSON d'entrée
        String requestBody = """
        {
          "description": "Test Task",
          "state": "TO_DO",
          "userId": 10
        }
        """;

        // On ne renvoie rien de particulier depuis le service (il n'y a pas de DTO en retour),
        // donc on n'a même pas besoin de stubbing (when/thenReturn) à ce stade.
        // Mais on peut vérifier que la méthode est appelée.

        mockMvc.perform(post("/api/projects/{projectId}/tasks", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk())  // Le controller renvoie 200 (OK) dans votre code
                .andExpect(jsonPath("$.message").value("Task added to project successfully"));

        // Vérification que la couche service a été appelée
        verify(taskService, times(1)).addTaskToProject(eq(projectId), any(TaskDto.class));
    }

    // ----------------------------------------------------------
    // 2) Test: changeTaskState (PUT /api/tasks/{taskId}/state)
    // ----------------------------------------------------------
    @Test
    @DisplayName("PUT /api/tasks/{taskId}/state -> 200 OK et message de succès")
    void testChangeTaskState() throws Exception {
        Long taskId = 100L;
        String requestBody = """
        {
          "newState": "DONE"
        }
        """;

        // Exécution
        mockMvc.perform(put("/api/tasks/{taskId}/state", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task state changed successfully"));

        // Vérification
        verify(taskService, times(1)).changeTaskState(taskId, "DONE");
    }

    // ----------------------------------------------------------
    // 3) Test: getAllTasksByProject (GET /api/projects/{projectId}/tasks)
    // ----------------------------------------------------------
    @Test
    @DisplayName("GET /api/projects/{projectId}/tasks -> 200 OK et liste de tâches")
    void testGetAllTasksByProject() throws Exception {
        Long projectId = 1L;

        // Préparation d'une liste simulée
        TaskDto t1 = new TaskDto();
        t1.setId(101L);
        t1.setDescription("Task 1");
        t1.setProjectId(projectId);

        TaskDto t2 = new TaskDto();
        t2.setId(102L);
        t2.setDescription("Task 2");
        t2.setProjectId(projectId);

        List<TaskDto> mockTasks = Arrays.asList(t1, t2);

        // Stubbing de la couche service
        when(taskService.getAllTasksByProject(projectId)).thenReturn(mockTasks);

        // Exécution
        mockMvc.perform(get("/api/projects/{projectId}/tasks", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(101L))
                .andExpect(jsonPath("$[0].description").value("Task 1"))
                .andExpect(jsonPath("$[1].id").value(102L))
                .andExpect(jsonPath("$[1].description").value("Task 2"));

        // Vérification
        verify(taskService, times(1)).getAllTasksByProject(projectId);
    }

    // ----------------------------------------------------------
    // 4) Test: assignUserToTask (PUT /api/tasks/{taskId}/assign/{userId})
    // ----------------------------------------------------------
    @Test
    @DisplayName("PUT /api/tasks/{taskId}/assign/{userId} -> 200 OK et message de succès")
    void testAssignUserToTask() throws Exception {
        Long taskId = 100L;
        Long userId = 50L;

        mockMvc.perform(put("/api/tasks/{taskId}/assign/{userId}", taskId, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task assigned successfully"));

        verify(taskService, times(1)).assignUserToTask(taskId, userId);
    }
}
