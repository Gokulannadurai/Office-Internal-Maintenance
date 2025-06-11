package com.maintenance.domain.schedule;

import com.maintenance.common.BaseService;

import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceScheduleService extends BaseService<MaintenanceSchedule, Long> {
    List<MaintenanceSchedule> findByStatus(ScheduleStatus status);
    List<MaintenanceSchedule> findByAssetId(Long assetId);
    List<MaintenanceSchedule> findByAssignedToId(Long assignedToId);
    List<MaintenanceSchedule> findByDateRange(LocalDateTime start, LocalDateTime end);
    void updateStatus(Long scheduleId, ScheduleStatus status);
    void assignSchedule(Long scheduleId, Long userId);
    void reschedule(Long scheduleId, LocalDateTime newDate);
    void processRecurringSchedules();
    List<MaintenanceSchedule> findOverdueSchedules();
    void markAsOverdue(Long scheduleId);
} 