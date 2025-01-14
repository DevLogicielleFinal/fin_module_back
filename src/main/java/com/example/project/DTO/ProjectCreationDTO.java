package com.example.project.DTO;

public class ProjectCreationDTO {
    private String name;
    private String description;

    // Constructeur
    public ProjectCreationDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters et setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
