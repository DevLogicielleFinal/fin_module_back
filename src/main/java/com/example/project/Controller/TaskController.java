package com.example.project.Controller;

import com.example.project.DTO.TaskDto;
import com.example.project.Service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskDto> addTaskToProject(
            @PathVariable("projectId") Long projectId,
            @RequestBody TaskDto taskDto
    ) {
        TaskDto createdTask = taskService.addTaskToProject(projectId, taskDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
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
    public ResponseEntity<TaskDto> assignUserToTask(
            @PathVariable("taskId") Long taskId,
            @PathVariable("userId") Long userId
    ) {
        TaskDto updatedTask = taskService.assignUserToTask(taskId, userId);
        return ResponseEntity.ok(updatedTask);
    }
}
