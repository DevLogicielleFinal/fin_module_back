package com.example.project.Service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    //private final String SECRET_KEY = "your-very-secret-key-here"; // Utilisez une clé sécurisée et gardez-la privée !
    private final int EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 heures

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Générer un JWT avec l'ID utilisateur comme sujet.
     *
     * @param userId l'ID de l'utilisateur
     * @return le token JWT généré
     */
    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extraire l'ID utilisateur (sujet) du JWT.
     *
     * @param token le token JWT
     * @return l'ID utilisateur
     */
    public String extractUserId(String token) {
        return getClaims(token).getSubject(); // Le sujet du JWT est généralement l'email
    }

    /**
     * Valider le JWT (signature, expiration, structure).
     *
     * @param token le token JWT
     * @return true si le token est valide, false sinon
     */
    public boolean validateToken(String token) {
        try {
            // Si l'analyse du jeton échoue, une exception sera levée
            getClaims(token); // Vérifie la signature du jeton
            return !isTokenExpired(token); // Vérifie si le jeton est expiré
        } catch (ExpiredJwtException e) {
            System.out.println("Le token est expiré");
        } catch (UnsupportedJwtException e) {
            System.out.println("Jeton non pris en charge");
        } catch (MalformedJwtException e) {
            System.out.println("Jeton mal formé");
        } catch (JwtException e) {
            // Capturer toutes les autres exceptions liées à JWT (y compris les erreurs de signature)
            System.out.println("Erreur lors de la validation du jeton : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur générale lors de la validation du jeton");
        }
        return false; // Le token est invalide ou a échoué aux vérifications
    }

    /**
     * Extraire les revendications (claims) du JWT.
     *
     * @param token le token JWT
     * @return les revendications extraites
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // Utiliser la même clé secrète pour valider le jeton
                .build()
                .parseClaimsJws(token) // Analyser le jeton
                .getBody(); // Extraire les revendications
    }

    /**
     * Vérifier si le token est expiré.
     *
     * @param token le token JWT
     * @return true si le token est expiré, false sinon
     */
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date()); // Comparer la date d'expiration avec la date actuelle
    }
}
