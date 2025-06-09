package com.maintenance.controller;

import com.maintenance.dto.UserDTO;
import com.maintenance.entity.User;
import com.maintenance.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
    }

    @Test
    void getAllUsers_ReturnsUserList() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");

        List<User> users = Arrays.asList(user1, user2);
        Page<User> userPage = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 10);

        when(userService.getAllUsers(pageable)).thenReturn(userPage);

        // Act
        ResponseEntity<Page<UserDTO>> response = userController.getAllUsers(pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    void getUserById_ExistingUser_ReturnsUser() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().getId());
        assertEquals(user.getUsername(), response.getBody().getUsername());
    }

    @Test
    void getUserById_NonExistingUser_ReturnsNotFound() {
        // Arrange
        Long userId = 999L;
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateUser_ValidUser_ReturnsUpdatedUser() {
        // Arrange
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("updateduser");
        userDTO.setEmail("updated@example.com");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername(userDTO.getUsername());
        updatedUser.setEmail(userDTO.getEmail());

        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(updatedUser);

        // Act
        ResponseEntity<UserDTO> response = userController.updateUser(userId, userDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().getId());
        assertEquals(userDTO.getUsername(), response.getBody().getUsername());
    }

    @Test
    void updateUser_NonExistingUser_ReturnsNotFound() {
        // Arrange
        Long userId = 999L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("updateduser");

        when(userService.updateUser(eq(userId), any(User.class)))
                .thenThrow(new RuntimeException("User not found"));

        // Act
        ResponseEntity<UserDTO> response = userController.updateUser(userId, userDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteUser_ExistingUser_ReturnsNoContent() {
        // Arrange
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(userId);
    }

    @Test
    void deleteUser_NonExistingUser_ReturnsNotFound() {
        // Arrange
        Long userId = 999L;
        doThrow(new RuntimeException("User not found")).when(userService).deleteUser(userId);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void searchUsers_ReturnsMatchingUsers() {
        // Arrange
        String searchTerm = "test";
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testuser1");
        user1.setEmail("test1@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");
        user2.setEmail("test2@example.com");

        List<User> users = Arrays.asList(user1, user2);
        Page<User> userPage = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 10);

        when(userService.searchUsers(searchTerm, pageable)).thenReturn(userPage);

        // Act
        ResponseEntity<Page<UserDTO>> response = userController.searchUsers(searchTerm, pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }
} 