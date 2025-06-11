package com.maintenance.domain.schedule;

import com.maintenance.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MaintenanceScheduleRepository extends BaseRepository<MaintenanceSchedule, Long> {
    List<MaintenanceSchedule> findByStatus(ScheduleStatus status);
    List<MaintenanceSchedule> findByAssetId(Long assetId);
    List<MaintenanceSchedule> findByAssignedToId(Long assignedToId);
    List<MaintenanceSchedule> findByScheduledDateBetween(LocalDateTime start, LocalDateTime end);
    List<MaintenanceSchedule> findByStatusAndScheduledDateBefore(ScheduleStatus status, LocalDateTime date);
    List<MaintenanceSchedule> findByRecurringPatternIsNotNull();
    List<MaintenanceSchedule> findByNextOccurrenceBefore(LocalDateTime date);
} 