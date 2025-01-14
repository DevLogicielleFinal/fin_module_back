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

    public TaskServiceImpl(TaskRepository taskRepository,
                           ProjectRepository projectRepository,
                           UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    /**
     * Ajouter une tâche à un projet avec gestion de la concurrence
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
     * Récupérer toutes les tâches d’un projet
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
     * Assigner un utilisateur à une tâche avec gestion de la concurrence
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
     * Mapper un DTO en entité Task
     */
    private Task mapToEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setId(taskDto.getId());  // s'il n'est pas null, sinon la DB le générera
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