package com.example.project.Controller;

import com.example.project.DTO.TaskDto;
import com.example.project.Service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * POST /api/projects/{projectId}/tasks
     * Créer une tâche et renvoyer un message de succès
     */
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Map<String, String>> addTaskToProject(
            @PathVariable("projectId") Long projectId,
            @RequestBody TaskDto taskDto
    ) {
        taskService.addTaskToProject(projectId, taskDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Task added to project successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/tasks/{taskId}/state
     * Changer l'état de la tâche.
     * Exemple de corps JSON : { "newState": "DONE" }
     */
    @PutMapping("/tasks/{taskId}/state")
    public ResponseEntity<Map<String, String>> changeTaskState(
            @PathVariable("taskId") Long taskId,
            @RequestBody Map<String, String> body
    ) {
        String newState = body.get("newState");
        taskService.changeTaskState(taskId, newState);

        // Crée un map JSON pour la réponse
        Map<String, String> response = new HashMap<>();
        response.put("message", "Task state changed successfully");

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasksByProject(
            @PathVariable("projectId") Long projectId
    ) {
        List<TaskDto> tasks = taskService.getAllTasksByProject(projectId);
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @PutMapping("/tasks/{taskId}/assign/{userId}")
    public ResponseEntity<Map<String, String>> assignUserToTask(
            @PathVariable("taskId") Long taskId,
            @PathVariable("userId") Long userId
    ) {
        taskService.assignUserToTask(taskId, userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Task assigned successfully");

        return ResponseEntity.ok(response);
    }
}
