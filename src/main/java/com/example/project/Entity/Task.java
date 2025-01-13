package com.example.project.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private StateTask state; // Enum representing the task state

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // The user assigned to the task

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project; // The project to which the task is linked

    public enum StateTask {
        TO_DO,
        IN_PROGRESS,
        DONE
    }

    // ---------------------------
    // Constructors
    // ---------------------------

    public Task() {
        // No-argument constructor (obligatoire pour JPA)
    }

    public Task(Long id, String description, LocalDate dueDate, StateTask state, User user, Project project) {
        this.id = id;
        this.description = description;
        this.dueDate = dueDate;
        this.state = state;
        this.user = user;
        this.project = project;
    }

    // ---------------------------
    // Getters and Setters
    // ---------------------------

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

    public StateTask getState() {
        return state;
    }

    public void setState(StateTask state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
