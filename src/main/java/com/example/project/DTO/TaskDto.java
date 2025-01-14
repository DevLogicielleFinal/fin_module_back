package com.example.project.DTO;

import java.time.LocalDate;

/**
 * Le DTO (Data Transfer Object) pour une tâche.
 * Cette classe est utilisée pour transférer les données d'une tâche,
 * y compris l'identifiant, la description, la date d'échéance, l'état,
 * l'utilisateur assigné et le projet auquel la tâche appartient.
 */
public class TaskDto {

    private Long id;
    private String description;
    private LocalDate dueDate;
    private String state;   // e.g. "TO_DO", "IN_PROGRESS", "DONE"
    private Long userId;    // ID of the user assigned to this task
    private Long projectId; // ID of the project to which the task belongs

    /**
     * Constructeur par défaut (sans arguments).
     */
    public TaskDto() {
    }

    /**
     * Constructeur avec tous les attributs de la tâche.
     *
     * @param id L'ID de la tâche.
     * @param description La description de la tâche.
     * @param dueDate La date d'échéance de la tâche.
     * @param state L'état de la tâche (par exemple, "TO_DO", "IN_PROGRESS", "DONE").
     * @param userId L'ID de l'utilisateur assigné à la tâche.
     * @param projectId L'ID du projet auquel la tâche appartient.
     */
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
    /**
     * Getter pour obtenir l'ID de la tâche.
     *
     * @return L'ID de la tâche.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter pour définir l'ID de la tâche.
     *
     * @param id L'ID de la tâche à définir.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter pour obtenir la description de la tâche.
     *
     * @return La description de la tâche.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter pour définir la description de la tâche.
     *
     * @param description La description de la tâche à définir.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter pour obtenir la date d'échéance de la tâche.
     *
     * @return La date d'échéance de la tâche.
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Setter pour définir la date d'échéance de la tâche.
     *
     * @param dueDate La date d'échéance de la tâche à définir.
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Getter pour obtenir l'état de la tâche.
     *
     * @return L'état de la tâche.
     */
    public String getState() {
        return state;
    }

    /**
     * Setter pour définir l'état de la tâche.
     *
     * @param state L'état de la tâche à définir (par exemple, "TO_DO", "IN_PROGRESS", "DONE").
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Getter pour obtenir l'ID de l'utilisateur assigné à la tâche.
     *
     * @return L'ID de l'utilisateur assigné.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Setter pour définir l'ID de l'utilisateur assigné à la tâche.
     *
     * @param userId L'ID de l'utilisateur assigné à la tâche.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Getter pour obtenir l'ID du projet auquel la tâche appartient.
     *
     * @return L'ID du projet.
     */
    public Long getProjectId() {
        return projectId;
    }

    /**
     * Setter pour définir l'ID du projet auquel la tâche appartient.
     *
     * @param projectId L'ID du projet auquel la tâche appartient.
     */
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'objet TaskDto.
     *
     * @return Une chaîne de caractères représentant l'objet TaskDto.
     */
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
