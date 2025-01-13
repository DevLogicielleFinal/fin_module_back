package com.example.project.Service;

import com.example.project.Entity.User;
import com.example.project.Repository.UserRepository;
import com.example.project.Security.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        // Initialisation d'un utilisateur de test
        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setPassword("plain_password"); // mot de passe pas encore encodé
    }

    // -------------------------------------------------------------------------
    // 1) Test de saveUser
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("saveUser - doit encoder le mot de passe et sauvegarder l'utilisateur")
    void testSaveUser() {
        // Arrange
        String encodedPassword = "encoded_password";
        when(passwordEncoder.encode("plain_password")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        verify(passwordEncoder).encode("plain_password");
        verify(userRepository).save(any(User.class));
        assertEquals(encodedPassword, savedUser.getPassword(),
                "Le mot de passe de l'utilisateur doit être encodé");
    }

    // -------------------------------------------------------------------------
    // 2) Test de authenticate
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("authenticate() tests")
    class AuthenticateTests {

        @Test
        @DisplayName("authenticate - doit renvoyer un token si les identifiants sont corrects")
        void testAuthenticateSuccess() {
            // Arrange
            user.setPassword("encoded_pass"); // ce qui est en BD
            when(userRepository.findByEmail("john@example.com"))
                    .thenReturn(Optional.of(user));
            when(passwordEncoder.matches("plain_password", "encoded_pass"))
                    .thenReturn(true);
            when(jwtUtil.generateToken(1L))
                    .thenReturn("jwt_token_example");

            // Act
            String token = userService.authenticate("john@example.com", "plain_password");

            // Assert
            assertEquals("jwt_token_example", token,
                    "Le token doit correspondre à celui généré par jwtUtil");
            verify(userRepository).findByEmail("john@example.com");
            verify(passwordEncoder).matches("plain_password", "encoded_pass");
            verify(jwtUtil).generateToken(1L);
        }

        @Test
        @DisplayName("authenticate - doit lever une exception si mot de passe incorrect")
        void testAuthenticateWrongPassword() {
            // Arrange
            user.setPassword("encoded_pass");
            when(userRepository.findByEmail("john@example.com"))
                    .thenReturn(Optional.of(user));
            when(passwordEncoder.matches("wrong_password", "encoded_pass"))
                    .thenReturn(false);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () ->
                            userService.authenticate("john@example.com", "wrong_password"),
                    "Doit lever IllegalArgumentException si le mot de passe est incorrect"
            );
            verify(userRepository).findByEmail("john@example.com");
            verify(passwordEncoder).matches("wrong_password", "encoded_pass");
        }

        @Test
        @DisplayName("authenticate - doit lever une exception si l'utilisateur n'existe pas")
        void testAuthenticateUserNotFound() {
            // Arrange
            when(userRepository.findByEmail("john@example.com"))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () ->
                            userService.authenticate("john@example.com", "plain_password"),
                    "Doit lever IllegalArgumentException si l'utilisateur n'est pas trouvé"
            );
            verify(userRepository).findByEmail("john@example.com");
            verifyNoMoreInteractions(passwordEncoder, jwtUtil);
        }
    }

    // -------------------------------------------------------------------------
    // 3) Test de findById
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("findById - doit retourner l'utilisateur s'il existe")
    void testFindByIdSuccess() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.findById(1L);

        // Assert
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("findById - doit lever une exception si l'utilisateur n'est pas trouvé")
    void testFindByIdNotFound() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.findById(99L));
        assertTrue(exception.getMessage().contains("Utilisateur avec l'ID 99 non trouvé"));
        verify(userRepository).findById(99L);
    }

    // -------------------------------------------------------------------------
    // 4) Test de getUsersByIds
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("getUsersByIds - doit appeler findAllById avec la liste d'IDs")
    void testGetUsersByIds() {
        // Arrange
        List<Long> userIds = Arrays.asList(1L, 2L, 3L);
        when(userRepository.findAllById(userIds)).thenReturn(Arrays.asList(user));

        // Act
        List<User> users = userService.getUsersByIds(userIds);

        // Assert
        verify(userRepository).findAllById(userIds);
        assertEquals(1, users.size(), "La liste retournée doit contenir un seul utilisateur (celui mocké)");
    }
}
