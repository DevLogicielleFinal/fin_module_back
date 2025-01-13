package com.example.project.Controller;

import com.example.project.Entity.User;
import com.example.project.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void registerUser_WithValidData_Should_Return_SavedUser() throws Exception {
        // Préparation des données
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userService.saveUser(any(User.class))).thenReturn(user);

        String requestBody = """
        {
            "email": "test@example.com",
            "password": "password123"
        }
    """;

        // Exécution et vérifications
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").value("password123"));

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    public void testProtectedRoute_WithValidToken_Should_Return_SuccessMessage() throws Exception {
        // Préparation des données
        String token = "Bearer validToken";

        // Exécution et vérifications
        mockMvc.perform(get("/users/protected")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Accès autorisé. Token valide : " + token));
    }
    @Test
    public void testProtectedRoute_WithoutToken_Should_ReturnBadRequest() throws Exception {
        // Exécution et vérifications
        mockMvc.perform(get("/users/protected"))
                .andExpect(status().isBadRequest());
    }
}
