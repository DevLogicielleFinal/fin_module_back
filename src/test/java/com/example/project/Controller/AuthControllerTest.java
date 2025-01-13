package com.example.project.Controller;

import com.example.project.Service.LoginRequest;
import com.example.project.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void login_With_Valid_Credentials_Returns_JwtToken() throws Exception {
        // Préparation des données
        String email = "test@example.com";
        String password = "password123";
        String jwt = "dummy-jwt-token";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Simulation du comportement du service
        when(userService.authenticate(email, password)).thenReturn(jwt);

        // Configuration de MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        // Corps de la requête JSON
        String requestBody = """
                {
                    "email": "test@example.com",
                    "password": "password123"
                }
                """;

        // Exécution de la requête POST et vérifications
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(jwt));
    }
}


