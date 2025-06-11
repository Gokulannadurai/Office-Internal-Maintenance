package com.maintenance.domain.schedule;

import com.maintenance.common.AbstractBaseEntity;
import com.maintenance.domain.asset.Asset;
import com.maintenance.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_schedule")
@Getter
@Setter
@SuperBuilder
public class MaintenanceSchedule extends AbstractBaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;
    
    @Column(name = "maintenance_type", nullable = false, length = 50)
    private String maintenanceType;
    
    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;
    
    @Column(name = "recurring_pattern")
    private String recurringPattern;
    
    @Column(name = "next_occurrence")
    private LocalDateTime nextOccurrence;
    
    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    public MaintenanceSchedule() {
        // Default constructor required by JPA
    }
} 