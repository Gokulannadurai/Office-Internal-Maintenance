package com.maintenance.domain.maintenance.dto;

import com.maintenance.domain.asset.dto.AssetDTO;
import com.maintenance.domain.maintenance.Priority;
import com.maintenance.domain.maintenance.RequestStatus;
import com.maintenance.domain.user.dto.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaintenanceRequestDTO {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private RequestStatus status;
    private UserDTO requester;
    private UserDTO assignedTo;
    private AssetDTO asset;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 