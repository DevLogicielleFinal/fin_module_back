package com.example.project.DTO;

import java.time.LocalDate;

/**
 * A Data Transfer Object (DTO) for the Task entity
 */
public class TaskDto {

    private Long id;
    private String description;
    private LocalDate dueDate;
    private String state;   // e.g. "TO_DO", "IN_PROGRESS", "DONE"
    private Long userId;    // ID of the user assigned to this task
    private Long projectId; // ID of the project to which the task belongs

    // Default constructor (no args)
    public TaskDto() {
    }

    // All-args constructor
    public TaskDto(Long id, String description, LocalDate dueDate,
                   String state, Long userId, Long projectId) {
        this.id = id;
        this.description = description;
        this.dueDate = dueDate;
        this.state = state;
        this.userId = userId;
        this.projectId = projectId;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", state='" + state + '\'' +
                ", userId=" + userId +
                ", projectId=" + projectId +
                '}';
    }
}
