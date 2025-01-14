package com.example.project.DTO;

/**
 * Le DTO (Data Transfer Object) pour la création d'un projet.
 * Cette classe est utilisée pour transférer les données nécessaires à la création d'un projet.
 */
public class ProjectCreationDTO {
    private String name;
    private String description;

    /**
     * Constructeur par défaut.
     * Utilisé pour l'instanciation de l'objet sans initialiser les attributs.
     */
    public ProjectCreationDTO() {
    }

    /**
     * Constructeur avec les attributs name et description.
     *
     * @param name La chaîne de caractères représentant le nom du projet.
     * @param description La chaîne de caractères représentant la description du projet.
     */
    public ProjectCreationDTO(String name, String description) {
        this.name = name;
        this.description = description;
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
}
