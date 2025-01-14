package com.example.project.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Représente une tâche dans un projet, associée à un utilisateur, avec une description,
 * une date limite, un état, et un projet auquel elle est rattachée.
 */
@Entity
public class Task {

    /**
     * Identifiant unique de la tâche.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Description de la tâche, expliquant ce qui doit être accompli.
     */
    private String description;

    /**
     * Date limite à laquelle la tâche doit être accomplie.
     */
    private LocalDate dueDate;

    /**
     * État actuel de la tâche (peut être "TO_DO", "IN_PROGRESS", "DONE").
     */
    @Enumerated(EnumType.STRING)
    private StateTask state; // Enum représentant l'état de la tâche

    /**
     * Utilisateur assigné à la tâche.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Utilisateur assigné à la tâche

    /**
     * Projet auquel cette tâche est associée.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project; // Projet lié à la tâche

    /**
     * Verrou de synchronisation pour gérer l'accès concurrent à la tâche.
     */
    @Transient // Non persisté en base
    private final Lock lock = new ReentrantLock();

    /**
     * Enum représentant les différents états d'une tâche.
     */
    public enum StateTask {
        TO_DO,
        IN_PROGRESS,
        DONE
    }

    // ---------------------------
    // Constructeurs
    // ---------------------------
    /**
     * Constructeur par défaut requis par JPA.
     */
    public Task() {
        // Constructeur sans argument (requis par JPA)
    }

    /**
     * Constructeur pour initialiser une tâche avec ses propriétés.
     *
     * @param id Description de l'identifiant.
     * @param description Description de la tâche.
     * @param dueDate Date limite de la tâche.
     * @param state État actuel de la tâche.
     * @param user Utilisateur assigné à la tâche.
     * @param project Projet auquel la tâche est liée.
     */
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
    /**
     * Verrouille la tâche de manière à éviter les modifications concurrentes.
     */
    public void lockTask() {
        lock.lock();
    }

    /**
     * Déverrouille la tâche après les modifications.
     */
    public void unlockTask() {
        lock.unlock();
    }

    // ---------------------------
    // Getters et Setters
    // ---------------------------
    /**
     * Retourne l'identifiant de la tâche.
     *
     * @return Identifiant de la tâche.
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant de la tâche.
     *
     * @param id Identifiant de la tâche.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retourne la description de la tâche.
     *
     * @return Description de la tâche.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit la description de la tâche.
     *
     * @param description Description de la tâche.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retourne la date limite de la tâche.
     *
     * @return Date limite de la tâche.
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Définit la date limite de la tâche.
     *
     * @param dueDate Date limite de la tâche.
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Retourne l'état actuel de la tâche.
     *
     * @return État de la tâche.
     */
    public StateTask getState() {
        return state;
    }

    /**
     * Définit l'état actuel de la tâche.
     *
     * @param state Nouvel état de la tâche.
     */
    public void setState(StateTask state) {
        this.state = state;
    }

    /**
     * Retourne l'utilisateur assigné à la tâche.
     *
     * @return Utilisateur assigné à la tâche.
     */
    public User getUser() {
        return user;
    }

    /**
     * Définit l'utilisateur assigné à la tâche.
     *
     * @param user Utilisateur assigné à la tâche.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retourne le projet auquel cette tâche est associée.
     *
     * @return Projet auquel la tâche est associée.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Définit le projet auquel cette tâche est associée.
     *
     * @param project Projet auquel la tâche est associée.
     */
    public void setProject(Project project) {
        this.project = project;
    }
}
