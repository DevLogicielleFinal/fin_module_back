package com.example.project.Service;
import com.example.project.DTO.ProjectCreationDTO;
import com.example.project.Entity.Project;
import com.example.project.Entity.User;
import com.example.project.Repository.ProjectRepository;
import com.example.project.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projetRepository;
    private final UserRepository utilisateurRepository;

    public ProjectService(ProjectRepository projetRepository, UserRepository utilisateurRepository) {
        this.projetRepository = projetRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<Project> getProjetsByUtilisateur(String username) {
        User utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return projetRepository.findByMembersContains(utilisateur);
    }

    public Project creerProjet(String username, ProjectCreationDTO projetDTO) {
        User utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Project projet = new Project();
        projet.setName(projetDTO.getName());
        projet.setDescription(projetDTO.getDescription());
        projet.setDateCreation(LocalDate.now());
        projet.setState(Project.EtatProjet.EN_COURS);

        // Ajoute l'utilisateur comme membre du projet
        projet.getMembers().add(utilisateur);

        return projetRepository.save(projet);
    }
}
