package com.example.project.DTO;

import java.time.LocalDate;
import java.util.List;

public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate dateCreation;
    private String state;
    private UserDTO creator;
    private List<UserDTO> members;

    // Constructeurs
    public ProjectDTO(Long id, String name, String description, LocalDate dateCreation, String state, UserDTO creator, List<UserDTO> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateCreation = dateCreation;
        this.state = state;
        this.creator = creator;
        this.members = members;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    public List<UserDTO> getMembers() {
        return members;
    }

    public void setMembers(List<UserDTO> members) {
        this.members = members;
    }
}
