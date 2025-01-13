package com.example.project;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDate dateLimite;

    @Enumerated(EnumType.STRING)
    private EtatTache etat;  // Enum représentant l'état de la tâche (to-do, en cours, terminé)

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private User utilisateur;  // L'utilisateur assigné à la tâche

    @ManyToOne
    @JoinColumn(name = "projet_id")
    private Project projet;  // Le projet auquel la tâche est liée

    // Getters et setters

    public enum EtatTache {
        TO_DO,
        EN_COURS,
        TERMINE
    }
}
