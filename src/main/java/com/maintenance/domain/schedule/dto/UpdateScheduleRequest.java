package com.maintenance.domain.schedule.dto;

import com.maintenance.domain.schedule.ScheduleStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateScheduleRequest {
    @Future(message = "Scheduled date must be in the future")
    private LocalDateTime scheduledDate;

    private Long assignedToId;

    private String recurringPattern;

    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    private Integer estimatedDurationMinutes;

    private String notes;

    private ScheduleStatus status;
} 