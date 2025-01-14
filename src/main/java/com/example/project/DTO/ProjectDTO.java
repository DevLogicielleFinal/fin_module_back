package com.example.project.DTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Le DTO (Data Transfer Object) pour un projet.
 * Cette classe est utilisée pour transférer les données d'un projet avec ses informations détaillées,
 * y compris l'identifiant, le nom, la description, la date de création, l'état, le créateur et les membres du projet.
 */
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate dateCreation;
    private String state;
    private UserDTO creator;
    private List<UserDTO> members;

    /**
     * Constructeur avec tous les attributs du projet.
     *
     * @param id L'ID du projet.
     * @param name Le nom du projet.
     * @param description La description du projet.
     * @param dateCreation La date de création du projet.
     * @param state L'état du projet (par exemple, "TO_DO", "IN_PROGRESS", "DONE").
     * @param creator Le créateur du projet.
     * @param members Liste des membres associés au projet.
     */
    public ProjectDTO(Long id, String name, String description, LocalDate dateCreation, String state, UserDTO creator, List<UserDTO> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateCreation = dateCreation;
        this.state = state;
        this.creator = creator;
        this.members = members;
    }

    /**
     * Getter pour obtenir l'ID du projet.
     *
     * @return L'ID du projet.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter pour définir l'ID du projet.
     *
     * @param id L'ID du projet à définir.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter pour obtenir le nom du projet.
     *
     * @return Le nom du projet.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter pour définir le nom du projet.
     *
     * @param name Le nom du projet à définir.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter pour obtenir la description du projet.
     *
     * @return La description du projet.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter pour définir la description du projet.
     *
     * @param description La description du projet à définir.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter pour obtenir la date de création du projet.
     *
     * @return La date de création du projet.
     */
    public LocalDate getDateCreation() {
        return dateCreation;
    }

    /**
     * Setter pour définir la date de création du projet.
     *
     * @param dateCreation La date de création du projet à définir.
     */
    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * Getter pour obtenir l'état du projet.
     *
     * @return L'état du projet.
     */
    public String getState() {
        return state;
    }

    /**
     * Setter pour définir l'état du projet.
     *
     * @param state L'état du projet à définir (par exemple, "TO_DO", "IN_PROGRESS", "DONE").
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Getter pour obtenir le créateur du projet.
     *
     * @return Le créateur du projet.
     */
    public UserDTO getCreator() {
        return creator;
    }

    /**
     * Setter pour définir le créateur du projet.
     *
     * @param creator Le créateur du projet à définir.
     */
    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    /**
     * Getter pour obtenir la liste des membres du projet.
     *
     * @return La liste des membres du projet.
     */
    public List<UserDTO> getMembers() {
        return members;
    }

    /**
     * Setter pour définir la liste des membres du projet.
     *
     * @param members La liste des membres du projet à définir.
     */
    public void setMembers(List<UserDTO> members) {
        this.members = members;
    }
}
