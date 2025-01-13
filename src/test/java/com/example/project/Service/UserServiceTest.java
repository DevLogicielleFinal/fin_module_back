package com.example.project.Service;

import com.example.project.Entity.User;
import com.example.project.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("plaintextpassword");
    }

    @Test
    public void saveUser_Should_Save_User_With_EncodedPassword() {
        // Préparation des données
        String encodedPassword = "encodedpassword";
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Exécution
        User savedUser = userService.saveUser(testUser);

        // Vérifications
        assertNotNull(savedUser);
        assertEquals(encodedPassword, testUser.getPassword());
        verify(passwordEncoder, times(1)).encode("plaintextpassword");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    public void authenticate_With_Valid_Credentials_ShouldReturnJwt() {
        // Préparation des données
        String rawPassword = "plaintextpassword";
        String encodedPassword = "encodedpassword";
        String jwt = "dummy-jwt-token";

        testUser.setPassword(encodedPassword);
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(testUser.getEmail())).thenReturn(jwt);

        // Exécution
        String result = userService.authenticate(testUser.getEmail(), rawPassword);

        // Vérifications
        assertNotNull(result);
        assertEquals(jwt, result);
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(jwtUtil, times(1)).generateToken(testUser.getEmail());
    }

    @Test
    public void authenticate_With_Invalid_Password_ShouldThrowException() {
        // Préparation des données
        String rawPassword = "wrongpassword";
        String encodedPassword = "encodedpassword";

        testUser.setPassword(encodedPassword);
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // Exécution et vérification
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.authenticate(testUser.getEmail(), rawPassword);
        });

        assertEquals("Mot de passe incorrect", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(jwtUtil, times(0)).generateToken(anyString());
    }

    @Test
    public void authenticate_With_Non_Existent_User_Should_ThrowException() {
        // Préparation des données
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        // Exécution et vérification
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.authenticate(testUser.getEmail(), testUser.getPassword());
        });

        assertEquals("Utilisateur non trouvé", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
        verify(jwtUtil, times(0)).generateToken(anyString());
    }
}
