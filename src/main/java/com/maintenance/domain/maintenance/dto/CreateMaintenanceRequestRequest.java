package com.maintenance.domain.maintenance.dto;

import com.maintenance.domain.maintenance.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMaintenanceRequestRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Asset ID is required")
    private Long assetId;
} 