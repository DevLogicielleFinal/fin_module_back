package com.example.project.Entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Représente un utilisateur du système avec des informations d'identification et de rôle.
 * L'utilisateur peut être associé à des projets qu'il a créés et auxquels il participe.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Identifiant unique de l'utilisateur.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom d'utilisateur unique, utilisé pour l'authentification.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Adresse e-mail unique de l'utilisateur.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Mot de passe de l'utilisateur (stocké de manière sécurisée).
     */
    @Column(nullable = false)
    private String password;

    /**
     * Rôle de l'utilisateur (peut être admin ou membre).
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Liste des projets créés par l'utilisateur.
     */
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> createdProjects = new HashSet<>();

    /**
     * Liste des projets auxquels l'utilisateur participe en tant que membre.
     */
    @ManyToMany(mappedBy = "members")
    private Set<Project> projets = new HashSet<>();  // Projets auxquels l'utilisateur participe

    /**
     * Liste des tâches assignées à l'utilisateur.
     */
    @OneToMany(mappedBy = "user")
    private Set<Task> taches = new HashSet<>();  // Tâches assignées à l'utilisateur

    /**
     * Constructeur par défaut.
     */
    public User() {}

    /**
     * Constructeur avec initialisation des informations de l'utilisateur.
     *
     * @param username Nom d'utilisateur.
     * @param email Adresse e-mail.
     * @param password Mot de passe.
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Enum représentant les rôles d'un utilisateur dans le système.
     */
    public enum Role {
        admin,
        member
    }

    /**
     * Retourne l'identifiant de l'utilisateur.
     *
     * @return Identifiant de l'utilisateur.
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant de l'utilisateur.
     *
     * @param id Identifiant de l'utilisateur.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retourne le nom d'utilisateur.
     *
     * @return Nom d'utilisateur.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Définit le nom d'utilisateur.
     *
     * @param name Nouveau nom d'utilisateur.
     */
    public void setUsername(String name) {
        this.username = name;
    }

    /**
     * Retourne l'adresse e-mail de l'utilisateur.
     *
     * @return Adresse e-mail de l'utilisateur.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse e-mail de l'utilisateur.
     *
     * @param mail Nouvelle adresse e-mail.
     */
    public void setEmail(String mail) {
        this.email = mail;
    }

    /**
     * Retourne le mot de passe de l'utilisateur.
     *
     * @return Mot de passe de l'utilisateur.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     *
     * @param password Nouveau mot de passe de l'utilisateur.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
