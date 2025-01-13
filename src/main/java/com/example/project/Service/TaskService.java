package com.example.project.Service;

import com.example.project.DTO.TaskDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    public TaskDto addTaskToProject(Long projectId, TaskDto taskDto);
    public List<TaskDto> getAllTasksByProject(Long projectId);
    public TaskDto assignUserToTask(Long taskId, Long userId);
}
