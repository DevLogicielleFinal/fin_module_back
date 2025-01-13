package com.example.project;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDate dateCreation;

    @Enumerated(EnumType.STRING)
    private EtatProjet state;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToMany
    @JoinTable(
            name = "projet_utilisateur",
            joinColumns = @JoinColumn(name = "projet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();  // Membres assignés au projet

    @OneToMany(mappedBy = "projet")
    private Set<Task> taches = new HashSet<>();

    public Project() {}

    // Constructeur avec arguments pour simplifier l'instanciation
    public Project(String nom, String description, LocalDate dateCreation, EtatProjet state) {
        this.name = nom;
        this.description = description;
        this.dateCreation = dateCreation;
        this.state = state;
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public enum EtatProjet {
        EN_COURS,
        TERMINE
    }
}