package com.maintenance.domain.user.dto;

import com.maintenance.domain.user.UserRole;
import com.maintenance.domain.user.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 