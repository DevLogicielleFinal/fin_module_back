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

/**
 * Représente un projet dans le système avec des informations liées à son nom, sa description,
 * sa date de création, son état, ses créateurs, ses membres et ses tâches associées.
 */
@Entity
public class Project {

    /**
     * Identifiant unique du projet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom du projet, doit être unique et avoir une taille entre 3 et 50 caractères.
     */
    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    /**
     * Description du projet, une chaîne de caractères ne dépassant pas 500 caractères.
     */
    @NotNull
    @Size(max = 500)
    private String description;

    /**
     * Date de création du projet.
     */
    private LocalDate dateCreation;


    /**
     * État actuel du projet (peut être "TO_DO", "EN_COURS", "TERMINE").
     */
    @Enumerated(EnumType.STRING)
    private EtatProjet state;

    /**
     * Utilisateur créateur du projet.
     */
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    /**
     * Membres participant au projet. Plusieurs utilisateurs peuvent être associés à un projet.
     */
    @ManyToMany
    @JoinTable(
            name = "projet_utilisateur",
            joinColumns = @JoinColumn(name = "projet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    /**
     * Liste des tâches associées au projet.
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> taches = new ArrayList<>();

    /**
     * Verrou de synchronisation pour assurer une gestion thread-safe lors des opérations sur les membres ou les tâches.
     */
    @Transient // Indique que cette propriété n'est pas persistée en base
    private final Lock lock = new ReentrantLock();

    /**
     * Constructeur par défaut.
     */
    public Project() {}

    /**
     * Constructeur avec initialisation des informations du projet.
     *
     * @param name Nom du projet.
     * @param description Description du projet.
     * @param dateCreation Date de création du projet.
     * @param state État du projet.
     * @param creator Utilisateur créateur du projet.
     */
    public Project(String name, String description, LocalDate dateCreation, EtatProjet state, User creator) {
        this.name = name;
        this.description = description;
        this.dateCreation = dateCreation;
        this.state = state;
        this.creator = creator;
    }

    /**
     * Retourne l'identifiant du projet.
     *
     * @return Identifiant du projet.
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant du projet.
     *
     * @param id Identifiant du projet.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retourne le nom du projet.
     *
     * @return Nom du projet.
     */
    public String getName() {
        return name;
    }

    /**
     * Définit le nom du projet.
     *
     * @param name Nom du projet.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retourne la description du projet.
     *
     * @return Description du projet.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit la description du projet.
     *
     * @param description Description du projet.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retourne la date de création du projet.
     *
     * @return Date de création du projet.
     */
    public LocalDate getDateCreation() {
        return dateCreation;
    }

    /**
     * Définit la date de création du projet.
     *
     * @param dateCreation Date de création du projet.
     */
    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * Retourne l'état du projet.
     *
     * @return État actuel du projet.
     */
    public EtatProjet getState() {
        return state;
    }

    /**
     * Définit l'état du projet.
     *
     * @param state Nouvel état du projet.
     */
    public void setState(EtatProjet state) {
        this.state = state;
    }

    /**
     * Retourne l'utilisateur créateur du projet.
     *
     * @return Utilisateur créateur du projet.
     */
    public User getCreator() {
        return creator;
    }

    /**
     * Définit l'utilisateur créateur du projet.
     *
     * @param creator Utilisateur créateur du projet.
     */
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

    /**
     * Retourne les membres participant au projet.
     *
     * @return Ensemble des membres du projet.
     */
    public Set<User> getMembers() {
        return members;
    }

    /**
     * Définit les membres participant au projet.
     *
     * @param members Ensemble des membres du projet.
     */
    public void setMembers(Set<User> members) {
        this.members = members;
    }

    /**
     * Ajoute un membre au projet de manière thread-safe.
     *
     * @param user Utilisateur à ajouter.
     */
    public void addMember(User user) {
        lock.lock();
        try {
            this.members.add(user);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retire un membre du projet de manière thread-safe.
     *
     * @param user Utilisateur à retirer.
     */
    public void removeMember(User user) {
        lock.lock();
        try {
            this.members.remove(user);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Ajoute une tâche au projet de manière thread-safe.
     *
     * @param task Tâche à ajouter.
     */
    public void addTask(Task task) {
        lock.lock();
        try {
            this.taches.add(task);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retire une tâche du projet de manière thread-safe.
     *
     * @param task Tâche à retirer.
     */
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