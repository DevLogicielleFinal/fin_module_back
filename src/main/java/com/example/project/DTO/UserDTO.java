package com.example.project.DTO;

/**
 * Le DTO (Data Transfer Object) pour un utilisateur.
 * Cette classe est utilisée pour transférer les données d'un utilisateur,
 * telles que l'ID, le nom d'utilisateur et l'email.
 */
public class UserDTO {
    private Long id;
    private String username;
    private String email;

    /**
     * Constructeur pour initialiser un objet UserDTO avec l'ID, le nom d'utilisateur et l'email.
     *
     * @param id L'ID de l'utilisateur.
     * @param username Le nom d'utilisateur.
     * @param email L'email de l'utilisateur.
     */
    public UserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    /**
     * Getter pour obtenir l'ID de l'utilisateur.
     *
     * @return L'ID de l'utilisateur.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter pour définir l'ID de l'utilisateur.
     *
     * @param id L'ID de l'utilisateur à définir.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter pour obtenir le nom d'utilisateur.
     *
     * @return Le nom d'utilisateur.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter pour définir le nom d'utilisateur.
     *
     * @param username Le nom d'utilisateur à définir.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter pour obtenir l'email de l'utilisateur.
     *
     * @return L'email de l'utilisateur.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter pour définir l'email de l'utilisateur.
     *
     * @param email L'email de l'utilisateur à définir.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
