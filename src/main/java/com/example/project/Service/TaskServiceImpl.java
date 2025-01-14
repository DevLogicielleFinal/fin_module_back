package com.example.project.Service;

import com.example.project.DTO.TaskDto;
import com.example.project.Entity.Project;
import com.example.project.Entity.Task;
import com.example.project.Entity.User;
import com.example.project.Repository.ProjectRepository;
import com.example.project.Repository.TaskRepository;
import com.example.project.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des tâches.
 * Cette classe contient les méthodes pour ajouter, récupérer et assigner des tâches aux projets et aux utilisateurs.
 * Elle implémente l'interface TaskService.
 */
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    /**
     * Constructeur de la classe TaskServiceImpl.
     *
     * @param taskRepository Le repository pour accéder aux tâches.
     * @param projectRepository Le repository pour accéder aux projets.
     * @param userRepository Le repository pour accéder aux utilisateurs.
     */
    public TaskServiceImpl(TaskRepository taskRepository,
                           ProjectRepository projectRepository,
                           UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    /**
     * Ajouter une tâche à un projet avec gestion de la concurrence.
     * Si l'utilisateur spécifié existe, la tâche sera assignée à cet utilisateur.
     * Si l'état de la tâche n'est pas précisé, l'état par défaut sera 'TO_DO'.
     *
     * @param projectId L'ID du projet auquel ajouter la tâche.
     * @param taskDto Les informations de la tâche à ajouter.
     * @return Le DTO de la tâche ajoutée.
     * @throws ProjectNotFoundException Si le projet n'est pas trouvé.
     * @throws UserNotFoundException Si l'utilisateur spécifié n'est pas trouvé.
     */
    @Override
    public TaskDto addTaskToProject(Long projectId, TaskDto taskDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        Task task = mapToEntity(taskDto);
        task.setProject(project);

        if (task.getState() == null) {
            task.setState(Task.StateTask.TO_DO);
        }

        if (taskDto.getUserId() != null) {
            User user = userRepository.findById(taskDto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + taskDto.getUserId()));
            task.setUser(user);
        }

        try {
            task.lockTask();
            Task savedTask = taskRepository.save(task);
            return mapToDto(savedTask);
        } finally {
            task.unlockTask();
        }
    }

    /**
     * Récupérer toutes les tâches associées à un projet.
     *
     * @param projectId L'ID du projet.
     * @return Une liste de DTO de tâches associées au projet.
     * @throws ProjectNotFoundException Si le projet n'est pas trouvé.
     */
    @Override
    public List<TaskDto> getAllTasksByProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }

        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return tasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Assigner un utilisateur à une tâche avec gestion de la concurrence.
     * Cette méthode associe un utilisateur à une tâche en vérifiant que la tâche et l'utilisateur existent.
     *
     * @param taskId L'ID de la tâche.
     * @param userId L'ID de l'utilisateur à associer à la tâche.
     * @return Le DTO de la tâche mise à jour.
     * @throws TaskNotFoundException Si la tâche n'est pas trouvée.
     * @throws UserNotFoundException Si l'utilisateur n'est pas trouvé.
     */
    @Override
    public TaskDto assignUserToTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        try {
            task.lockTask();
            task.setUser(user);
            Task savedTask = taskRepository.save(task);
            return mapToDto(savedTask);
        } finally {
            task.unlockTask();
        }
    }

    /**
     * Changer l'état d'une tâche.
     *
     * @param taskId L'ID de la tâche dont l'état doit être modifié.
     * @param newState Le nouvel état sous forme de chaîne (ex. "TO_DO", "IN_PROGRESS", "DONE").
     * @return Le DTO de la tâche mise à jour.
     * @throws TaskNotFoundException Si la tâche n'est pas trouvée.
     * @throws RuntimeException Si l'état est invalide.
     */
    public TaskDto changeTaskState(Long taskId, String newState) {
        // Récupérer la tâche
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(
                        "Task not found with id: " + taskId
                ));

        // Convertir la chaine 'newState' en valeur de l'enum
        try {
            Task.StateTask state = Task.StateTask.valueOf(newState);
            task.setState(state);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid state: " + newState);
        }

        // Sauvegarder la tâche
        Task savedTask = taskRepository.save(task);

        // Retourner un TaskDto mis à jour
        return mapToDto(savedTask);
    }

    /**
     * Mapper une entité Task en un DTO TaskDto.
     *
     * @param task L'entité Task à mapper.
     * @return Le DTO TaskDto correspondant.
     */
    private TaskDto mapToDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        if (task.getState() != null) {
            dto.setState(task.getState().name());
        }
        if (task.getUser() != null) {
            dto.setUserId(task.getUser().getId());
        }
        if (task.getProject() != null) {
            dto.setProjectId(task.getProject().getId());
        }
        return dto;
    }

    /**
     * Mapper un DTO TaskDto en une entité Task.
     *
     * @param taskDto Le DTO TaskDto à mapper.
     * @return L'entité Task correspondante.
     */
    private Task mapToEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        if (taskDto.getState() != null) {
            task.setState(Task.StateTask.valueOf(taskDto.getState()));
        }
        return task;
    }

    // Exceptions internes
    public static class ProjectNotFoundException extends RuntimeException {
        public ProjectNotFoundException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class TaskNotFoundException extends RuntimeException {
        public TaskNotFoundException(String message) {
            super(message);
        }
    }
}