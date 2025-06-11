package com.maintenance.domain.schedule;

import com.maintenance.common.AbstractBaseService;
import com.maintenance.domain.asset.Asset;
import com.maintenance.domain.asset.AssetService;
import com.maintenance.domain.user.User;
import com.maintenance.domain.user.UserService;
import com.maintenance.exception.BusinessException;
import com.maintenance.exception.NotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaintenanceScheduleServiceImpl extends AbstractBaseService<MaintenanceSchedule, Long, MaintenanceScheduleRepository> 
        implements MaintenanceScheduleService {

    private final UserService userService;
    private final AssetService assetService;

    public MaintenanceScheduleServiceImpl(
            MaintenanceScheduleRepository repository,
            UserService userService,
            AssetService assetService) {
        super(repository);
        this.userService = userService;
        this.assetService = assetService;
    }

    @Override
    public List<MaintenanceSchedule> findByStatus(ScheduleStatus status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<MaintenanceSchedule> findByAssetId(Long assetId) {
        return repository.findByAssetId(assetId);
    }

    @Override
    public List<MaintenanceSchedule> findByAssignedToId(Long assignedToId) {
        return repository.findByAssignedToId(assignedToId);
    }

    @Override
    public List<MaintenanceSchedule> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return repository.findByScheduledDateBetween(start, end);
    }

    @Override
    @Transactional
    public void updateStatus(Long scheduleId, ScheduleStatus status) {
        MaintenanceSchedule schedule = findById(scheduleId);
        validateStatusTransition(schedule.getStatus(), status);
        
        schedule.setStatus(status);
        if (status == ScheduleStatus.IN_PROGRESS) {
            assetService.updateStatus(schedule.getAsset().getId(), AssetStatus.UNDER_MAINTENANCE);
        } else if (status == ScheduleStatus.COMPLETED) {
            assetService.updateStatus(schedule.getAsset().getId(), AssetStatus.OPERATIONAL);
        }
        
        repository.save(schedule);
    }

    @Override
    @Transactional
    public void assignSchedule(Long scheduleId, Long userId) {
        MaintenanceSchedule schedule = findById(scheduleId);
        User user = userService.findById(userId);
        
        if (schedule.getStatus() != ScheduleStatus.SCHEDULED) {
            throw new BusinessException("Can only assign scheduled maintenance tasks", "INVALID_SCHEDULE_STATUS");
        }
        
        schedule.setAssignedTo(user);
        repository.save(schedule);
    }

    @Override
    @Transactional
    public void reschedule(Long scheduleId, LocalDateTime newDate) {
        MaintenanceSchedule schedule = findById(scheduleId);
        
        if (schedule.getStatus() == ScheduleStatus.COMPLETED) {
            throw new BusinessException("Cannot reschedule completed maintenance tasks", "INVALID_SCHEDULE_STATUS");
        }
        
        schedule.setScheduledDate(newDate);
        schedule.setStatus(ScheduleStatus.RESCHEDULED);
        repository.save(schedule);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    @Transactional
    public void processRecurringSchedules() {
        List<MaintenanceSchedule> recurringSchedules = repository.findByRecurringPatternIsNotNull();
        LocalDateTime now = LocalDateTime.now();
        
        for (MaintenanceSchedule schedule : recurringSchedules) {
            if (schedule.getNextOccurrence() != null && schedule.getNextOccurrence().isBefore(now)) {
                // Create a new schedule for the next occurrence
                MaintenanceSchedule newSchedule = MaintenanceSchedule.builder()
                        .asset(schedule.getAsset())
                        .maintenanceType(schedule.getMaintenanceType())
                        .scheduledDate(schedule.getNextOccurrence())
                        .status(ScheduleStatus.SCHEDULED)
                        .assignedTo(schedule.getAssignedTo())
                        .recurringPattern(schedule.getRecurringPattern())
                        .estimatedDurationMinutes(schedule.getEstimatedDurationMinutes())
                        .notes(schedule.getNotes())
                        .build();
                
                // Calculate next occurrence
                newSchedule.setNextOccurrence(calculateNextOccurrence(schedule.getNextOccurrence(), schedule.getRecurringPattern()));
                
                repository.save(newSchedule);
            }
        }
    }

    @Override
    public List<MaintenanceSchedule> findOverdueSchedules() {
        return repository.findByStatusAndScheduledDateBefore(ScheduleStatus.SCHEDULED, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void markAsOverdue(Long scheduleId) {
        MaintenanceSchedule schedule = findById(scheduleId);
        if (schedule.getStatus() == ScheduleStatus.SCHEDULED) {
            schedule.setStatus(ScheduleStatus.OVERDUE);
            repository.save(schedule);
        }
    }

    @Override
    @Transactional
    public MaintenanceSchedule create(MaintenanceSchedule schedule) {
        validateSchedule(schedule);
        return super.create(schedule);
    }

    private void validateSchedule(MaintenanceSchedule schedule) {
        if (schedule.getScheduledDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Cannot schedule maintenance in the past", "INVALID_SCHEDULE_DATE");
        }
        
        if (schedule.getRecurringPattern() != null) {
            schedule.setNextOccurrence(calculateNextOccurrence(schedule.getScheduledDate(), schedule.getRecurringPattern()));
        }
    }

    private void validateStatusTransition(ScheduleStatus currentStatus, ScheduleStatus newStatus) {
        if (currentStatus == ScheduleStatus.COMPLETED) {
            throw new BusinessException("Cannot change status of completed maintenance tasks", "INVALID_STATUS_CHANGE");
        }
        
        if (currentStatus == ScheduleStatus.CANCELLED && newStatus != ScheduleStatus.RESCHEDULED) {
            throw new BusinessException("Cancelled tasks can only be rescheduled", "INVALID_STATUS_CHANGE");
        }
    }

    private LocalDateTime calculateNextOccurrence(LocalDateTime currentDate, String recurringPattern) {
        // This is a simplified implementation. In a real system, you would parse the recurring pattern
        // and calculate the next occurrence based on the pattern (e.g., daily, weekly, monthly)
        return currentDate.plusDays(7); // Default to weekly recurrence
    }
} 