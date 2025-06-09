package com.maintenance.controller;

import com.maintenance.dto.LoginRequest;
import com.maintenance.dto.SignupRequest;
import com.maintenance.dto.UserDTO;
import com.maintenance.entity.User;
import com.maintenance.security.JwtTokenProvider;
import com.maintenance.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationManager, tokenProvider, userService);
    }

    @Test
    void login_ValidCredentials_ReturnsToken() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        String expectedToken = "jwt.token.here";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn(expectedToken);

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof java.util.Map);
        java.util.Map<?, ?> responseBody = (java.util.Map<?, ?>) response.getBody();
        assertEquals(expectedToken, responseBody.get("token"));
        assertEquals("Bearer", responseBody.get("type"));
    }

    @Test
    void login_InvalidCredentials_ReturnsUnauthorized() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void signup_ValidRequest_ReturnsCreatedUser() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newuser");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password");

        User createdUser = new User();
        createdUser.setUsername(signupRequest.getUsername());
        createdUser.setEmail(signupRequest.getEmail());
        createdUser.setRole("USER");

        when(userService.createUser(any(User.class))).thenReturn(createdUser);

        // Act
        ResponseEntity<?> response = authController.signup(signupRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof UserDTO);
        UserDTO responseUser = (UserDTO) response.getBody();
        assertEquals(signupRequest.getUsername(), responseUser.getUsername());
        assertEquals(signupRequest.getEmail(), responseUser.getEmail());
    }

    @Test
    void signup_DuplicateUsername_ReturnsBadRequest() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("existinguser");
        signupRequest.setEmail("existinguser@example.com");
        signupRequest.setPassword("password");

        when(userService.createUser(any(User.class)))
                .thenThrow(new RuntimeException("Username already exists"));

        // Act
        ResponseEntity<?> response = authController.signup(signupRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void signup_InvalidEmail_ReturnsBadRequest() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newuser");
        signupRequest.setEmail("invalid-email");
        signupRequest.setPassword("password");

        // Act
        ResponseEntity<?> response = authController.signup(signupRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void signup_ShortPassword_ReturnsBadRequest() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newuser");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("short");

        // Act
        ResponseEntity<?> response = authController.signup(signupRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
} 