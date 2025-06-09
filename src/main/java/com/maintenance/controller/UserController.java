package com.maintenance.controller;

import com.maintenance.dto.ApiResponse;
import com.maintenance.dto.UserDTO;
import com.maintenance.entity.User;
import com.maintenance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.createUser(userDTO.toEntity());
        return ResponseEntity.ok(ApiResponse.success("User created successfully", UserDTO.fromEntity(user)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        User user = userService.updateUser(id, userDTO.toEntity());
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", UserDTO.fromEntity(user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(UserDTO.fromEntity(user)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        Page<UserDTO> userDTOs = users.map(UserDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(userDTOs));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> searchUsers(
            @RequestParam String searchTerm,
            Pageable pageable) {
        Page<User> users = userService.searchUsers(searchTerm, pageable);
        Page<UserDTO> userDTOs = users.map(UserDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(userDTOs));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getUsersByRole(
            @PathVariable String role,
            Pageable pageable) {
        Page<User> users = userService.getUsersByRole(role, pageable);
        Page<UserDTO> userDTOs = users.map(UserDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(userDTOs));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getActiveUsers(Pageable pageable) {
        Page<User> users = userService.getActiveUsers(pageable);
        Page<UserDTO> userDTOs = users.map(UserDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(userDTOs));
    }
} 