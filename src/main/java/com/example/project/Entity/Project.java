package com.example.project.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    @Size(max = 500)
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
    private Set<User> members = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> taches = new ArrayList<>();

    @Transient // Indique que cette propriété n'est pas persistée en base
    private final Lock lock = new ReentrantLock();

    public Project() {}

    // Constructeur avec arguments pour simplifier l'instanciation
    public Project(String name, String description, LocalDate dateCreation, EtatProjet state, User creator) {
        this.name = name;
        this.description = description;
        this.dateCreation = dateCreation;
        this.state = state;
        this.creator = creator;
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

    public EtatProjet getState() {
        return state;
    }

    public void setState(EtatProjet state) {
        this.state = state;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    // Méthodes pour la gestion des membres et des tâches avec verrouillage sécurisé

    public void lockProject() {
        lock.lock();
    }
    public void unlockProject() {
        lock.unlock();
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public void addMember(User user) {
        lock.lock();
        try {
            this.members.add(user);
        } finally {
            lock.unlock();
        }
    }

    public void removeMember(User user) {
        lock.lock();
        try {
            this.members.remove(user);
        } finally {
            lock.unlock();
        }
    }

    public void addTask(Task task) {
        lock.lock();
        try {
            this.taches.add(task);
        } finally {
            lock.unlock();
        }
    }

    public void removeTask(Task task) {
        lock.lock();
        try {
            this.taches.remove(task);
        } finally {
            lock.unlock();
        }
    }

    public enum EtatProjet {
        TO_DO,
        EN_COURS,
        TERMINE
    }
}