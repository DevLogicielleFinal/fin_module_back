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

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // Constructeur sans Lombok
    public TaskServiceImpl(TaskRepository taskRepository,
                           ProjectRepository projectRepository,
                           UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    /**
     * 1. Créer une nouvelle tâche et l’associer à un projet
     */
    public TaskDto addTaskToProject(Long projectId, TaskDto taskDto) {

        // 1. Récupérer le projet
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(
                        "Project not found with id: " + projectId
                ));

        // 2. Mapper le TaskDto en entité Task
        Task task = mapToEntity(taskDto);
        task.setProject(project);

        if (task.getState() == null) {
            task.setState(Task.StateTask.TO_DO);
        }

        // 3. Si un userId est présent dans le DTO, on assigne l'utilisateur
        if (taskDto.getUserId() != null) {
            User user = userRepository.findById(taskDto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(
                            "User not found with id: " + taskDto.getUserId()
                    ));
            task.setUser(user);
        }

        // 4. Persister la tâche
        Task savedTask = taskRepository.save(task);

        // 5. Retourner la tâche persistée sous forme de DTO
        return mapToDto(savedTask);
    }

    /**
     * 2. Récupérer toutes les tâches d’un projet
     */
    public List<TaskDto> getAllTasksByProject(Long projectId) {
        // Vérifier l'existence du projet
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }

        // Récupérer les tâches du projet
        List<Task> tasks = taskRepository.findByProjectId(projectId);

        return tasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * 3. Assigner un utilisateur à une tâche
     */
    public TaskDto assignUserToTask(Long taskId, Long userId) {
        // Récupérer la tâche
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(
                        "Task not found with id: " + taskId
                ));

        // Récupérer l’utilisateur
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id: " + userId
                ));

        // Assigner l’utilisateur
        task.setUser(user);
        Task savedTask = taskRepository.save(task);

        return mapToDto(savedTask);
    }

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

    private Task mapToEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setId(taskDto.getId());  // s'il n'est pas null, sinon la DB le générera
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());

        // Conversion du state (String -> Enum) si présent
        if (taskDto.getState() != null) {
            task.setState(Task.StateTask.valueOf(taskDto.getState()));
        }
        return task;
    }

    // ---------------------------------------------------------
    // Exceptions internes (ou déplacez-les dans un package "exception")
    // ---------------------------------------------------------

    public class ProjectNotFoundException extends RuntimeException {
        public ProjectNotFoundException(String message) {
            super(message);
        }
    }

    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public class TaskNotFoundException extends RuntimeException {
        public TaskNotFoundException(String message) {
            super(message);
        }
    }
}