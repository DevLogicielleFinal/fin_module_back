package com.example.project.Service;

import com.example.project.DTO.TaskDto;
import com.example.project.Entity.Project;
import com.example.project.Entity.Task;
import com.example.project.Entity.User;
import com.example.project.Repository.ProjectRepository;
import com.example.project.Repository.TaskRepository;
import com.example.project.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests pour TaskServiceImpl (sans lockTask/unlockTask).
 */
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Project project;
    private User user;
    private Task task;
    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        // Initialiser un Project (id = 1)
        project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        // Initialiser un User (id = 10)
        user = new User();
        user.setId(10L);
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setPassword("encoded_pwd");

        // Initialiser une Task
        task = new Task();
        task.setId(100L);
        task.setDescription("Test Task");
        task.setState(Task.StateTask.TO_DO);
        task.setProject(project);

        // Initialiser un TaskDto
        taskDto = new TaskDto();
        taskDto.setId(100L);
        taskDto.setDescription("DTO Task");
        taskDto.setState("TO_DO");
        taskDto.setProjectId(1L);
        taskDto.setUserId(null); // pas d'utilisateur pour l'instant
    }

    // -------------------------------------------------------------------------
    // 1) Test addTaskToProject
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("addTaskToProject tests")
    class AddTaskToProjectTests {

        @Test
        @DisplayName("Doit créer la tâche et la sauvegarder si le projet existe")
        void testAddTaskToProjectSuccess() {
            // Arrange
            when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
            when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
                Task argTask = invocation.getArgument(0);
                argTask.setId(999L); // simulons un ID généré par la BD
                return argTask;
            });

            // Act
            TaskDto result = taskService.addTaskToProject(1L, taskDto);

            // Assert
            verify(projectRepository).findById(1L);
            verify(taskRepository).save(any(Task.class));

            assertNotNull(result, "Le TaskDto retourné ne doit pas être null");
            assertEquals(999L, result.getId(), "L'ID doit correspondre à celui généré par la BD (999)");
            assertEquals("DTO Task", result.getDescription());
            assertEquals("TO_DO", result.getState());
            assertEquals(1L, result.getProjectId());
            assertNull(result.getUserId(), "Pas d'utilisateur assigné");
        }

        @Test
        @DisplayName("Doit lever ProjectNotFoundException si le projet n'existe pas")
        void testAddTaskToProjectProjectNotFound() {
            // Arrange
            when(projectRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(
                    TaskServiceImpl.ProjectNotFoundException.class,
                    () -> taskService.addTaskToProject(1L, taskDto)
            );

            verify(projectRepository).findById(1L);
            verify(taskRepository, never()).save(any(Task.class));
        }

        @Test
        @DisplayName("Doit assigner un user s'il est précisé dans le DTO")
        void testAddTaskToProjectWithUser() {
            // Arrange
            taskDto.setUserId(10L); // On veut assigner l'utilisateur #10
            when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
            when(userRepository.findById(10L)).thenReturn(Optional.of(user));
            when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            TaskDto result = taskService.addTaskToProject(1L, taskDto);

            // Assert
            verify(userRepository).findById(10L);
            verify(taskRepository).save(any(Task.class));
            assertEquals(10L, result.getUserId(), "Le userId dans le DTO doit être 10");
        }

        @Test
        @DisplayName("Doit lever UserNotFoundException si userId n'existe pas")
        void testAddTaskToProjectUserNotFound() {
            // Arrange
            taskDto.setUserId(9999L);
            when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
            when(userRepository.findById(9999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(
                    TaskServiceImpl.UserNotFoundException.class,
                    () -> taskService.addTaskToProject(1L, taskDto)
            );

            verify(projectRepository).findById(1L);
            verify(userRepository).findById(9999L);
            verify(taskRepository, never()).save(any(Task.class));
        }
    }

    // -------------------------------------------------------------------------
    // 2) Test getAllTasksByProject
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("getAllTasksByProject tests")
    class GetAllTasksByProjectTests {

        @Test
        @DisplayName("Doit retourner toutes les tâches d'un projet existant")
        void testGetAllTasksSuccess() {
            when(projectRepository.existsById(1L)).thenReturn(true);

            Task t1 = new Task();
            t1.setId(101L);
            t1.setDescription("Task 1");
            t1.setProject(project);

            Task t2 = new Task();
            t2.setId(102L);
            t2.setDescription("Task 2");
            t2.setProject(project);

            when(taskRepository.findByProjectId(1L)).thenReturn(Arrays.asList(t1, t2));

            // Act
            List<TaskDto> result = taskService.getAllTasksByProject(1L);

            // Assert
            verify(projectRepository).existsById(1L);
            verify(taskRepository).findByProjectId(1L);

            assertEquals(2, result.size(), "La liste retournée doit contenir 2 éléments");
            assertEquals(101L, result.get(0).getId());
            assertEquals(102L, result.get(1).getId());
        }

        @Test
        @DisplayName("Doit lever ProjectNotFoundException si le projet n'existe pas")
        void testGetAllTasksProjectNotFound() {
            when(projectRepository.existsById(1L)).thenReturn(false);

            assertThrows(
                    TaskServiceImpl.ProjectNotFoundException.class,
                    () -> taskService.getAllTasksByProject(1L)
            );

            verify(projectRepository).existsById(1L);
            verify(taskRepository, never()).findByProjectId(anyLong());
        }
    }

    // -------------------------------------------------------------------------
    // 3) Test assignUserToTask
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("assignUserToTask tests")
    class AssignUserToTaskTests {

        @Test
        @DisplayName("Doit assigner un user à une tâche si les deux existent")
        void testAssignUserToTaskSuccess() {
            when(taskRepository.findById(100L)).thenReturn(Optional.of(task));
            when(userRepository.findById(10L)).thenReturn(Optional.of(user));
            when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

            TaskDto result = taskService.assignUserToTask(100L, 10L);

            verify(taskRepository).findById(100L);
            verify(userRepository).findById(10L);
            verify(taskRepository).save(any(Task.class));

            assertNotNull(result);
            assertEquals(100L, result.getId(), "L'ID de la tâche doit rester 100");
            assertEquals(10L, result.getUserId(), "L'utilisateur assigné doit être 10");
        }

        @Test
        @DisplayName("Doit lever TaskNotFoundException si la tâche n'existe pas")
        void testAssignUserTaskNotFound() {
            when(taskRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(
                    TaskServiceImpl.TaskNotFoundException.class,
                    () -> taskService.assignUserToTask(999L, 10L)
            );

            verify(taskRepository).findById(999L);
            verifyNoMoreInteractions(userRepository);
            verify(taskRepository, never()).save(any(Task.class));
        }

        @Test
        @DisplayName("Doit lever UserNotFoundException si l'utilisateur n'existe pas")
        void testAssignUserUserNotFound() {
            when(taskRepository.findById(100L)).thenReturn(Optional.of(task));
            when(userRepository.findById(9999L)).thenReturn(Optional.empty());

            assertThrows(
                    TaskServiceImpl.UserNotFoundException.class,
                    () -> taskService.assignUserToTask(100L, 9999L)
            );

            verify(taskRepository).findById(100L);
            verify(userRepository).findById(9999L);
            verify(taskRepository, never()).save(any(Task.class));
        }
    }

    // -------------------------------------------------------------------------
    // 4) Test changeTaskState
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("changeTaskState tests")
    class ChangeTaskStateTests {

        @Test
        @DisplayName("Doit changer l'état de la tâche si la tâche existe et l'état est valide")
        void testChangeTaskStateSuccess() {
            when(taskRepository.findById(100L)).thenReturn(Optional.of(task));
            when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

            TaskDto result = taskService.changeTaskState(100L, "DONE");

            verify(taskRepository).findById(100L);
            verify(taskRepository).save(task);

            assertEquals("DONE", result.getState());
        }

        @Test
        @DisplayName("Doit lever TaskNotFoundException si la tâche n'existe pas")
        void testChangeTaskStateNotFound() {
            when(taskRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(
                    TaskServiceImpl.TaskNotFoundException.class,
                    () -> taskService.changeTaskState(999L, "IN_PROGRESS")
            );

            verify(taskRepository).findById(999L);
            verify(taskRepository, never()).save(any(Task.class));
        }

        @Test
        @DisplayName("Doit lever RuntimeException si l'état est invalide")
        void testChangeTaskStateInvalid() {
            when(taskRepository.findById(100L)).thenReturn(Optional.of(task));

            // "INVALID_STATE" n'existe pas dans l'enum
            assertThrows(
                    RuntimeException.class,
                    () -> taskService.changeTaskState(100L, "INVALID_STATE")
            );

            verify(taskRepository).findById(100L);
            verify(taskRepository, never()).save(any(Task.class));
        }
    }
}
