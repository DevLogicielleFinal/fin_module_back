package com.example.project.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private StateTask state; // Enum représentant l'état de la tâche

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Utilisateur assigné à la tâche

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project; // Projet lié à la tâche

    @Transient // Non persisté en base
    private final Lock lock = new ReentrantLock();

    public enum StateTask {
        TO_DO,
        IN_PROGRESS,
        DONE
    }

    // ---------------------------
    // Constructeurs
    // ---------------------------
    public Task() {
        // Constructeur sans argument (requis par JPA)
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
    // Méthodes de gestion de verrouillage
    // ---------------------------
    public void lockTask() {
        lock.lock();
    }

    public void unlockTask() {
        lock.unlock();
    }

    // ---------------------------
    // Getters et Setters
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
