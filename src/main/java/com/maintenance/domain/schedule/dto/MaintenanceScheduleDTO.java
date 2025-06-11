package com.maintenance.domain.schedule.dto;

import com.maintenance.domain.asset.dto.AssetDTO;
import com.maintenance.domain.schedule.ScheduleStatus;
import com.maintenance.domain.user.dto.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaintenanceScheduleDTO {
    private Long id;
    private AssetDTO asset;
    private String maintenanceType;
    private LocalDateTime scheduledDate;
    private ScheduleStatus status;
    private UserDTO assignedTo;
    private String recurringPattern;
    private LocalDateTime nextOccurrence;
    private Integer estimatedDurationMinutes;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 