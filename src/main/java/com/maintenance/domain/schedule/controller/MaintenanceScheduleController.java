package com.maintenance.domain.schedule.controller;

import com.maintenance.domain.asset.Asset;
import com.maintenance.domain.asset.dto.AssetDTO;
import com.maintenance.domain.schedule.MaintenanceSchedule;
import com.maintenance.domain.schedule.ScheduleStatus;
import com.maintenance.domain.schedule.dto.CreateScheduleRequest;
import com.maintenance.domain.schedule.dto.MaintenanceScheduleDTO;
import com.maintenance.domain.schedule.dto.UpdateScheduleRequest;
import com.maintenance.domain.schedule.service.MaintenanceScheduleService;
import com.maintenance.domain.user.User;
import com.maintenance.domain.user.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/maintenance-schedules")
@RequiredArgsConstructor
public class MaintenanceScheduleController {

    private final MaintenanceScheduleService maintenanceScheduleService;

    @PostMapping
    public ResponseEntity<MaintenanceScheduleDTO> createSchedule(
            @Valid @RequestBody CreateScheduleRequest request) {
        MaintenanceSchedule schedule = maintenanceScheduleService.createSchedule(request);
        return ResponseEntity.ok(convertToDTO(schedule));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceScheduleDTO> getSchedule(@PathVariable Long id) {
        MaintenanceSchedule schedule = maintenanceScheduleService.findById(id);
        return ResponseEntity.ok(convertToDTO(schedule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceScheduleDTO> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody UpdateScheduleRequest request) {
        MaintenanceSchedule schedule = maintenanceScheduleService.updateSchedule(id, request);
        return ResponseEntity.ok(convertToDTO(schedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        maintenanceScheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MaintenanceScheduleDTO>> getSchedulesByStatus(
            @PathVariable ScheduleStatus status) {
        List<MaintenanceSchedule> schedules = maintenanceScheduleService.findByStatus(status);
        return ResponseEntity.ok(schedules.stream()
                .map(this::convertToDTO)
                .toList());
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<MaintenanceScheduleDTO>> getSchedulesByAsset(
            @PathVariable Long assetId) {
        List<MaintenanceSchedule> schedules = maintenanceScheduleService.findByAssetId(assetId);
        return ResponseEntity.ok(schedules.stream()
                .map(this::convertToDTO)
                .toList());
    }

    @GetMapping("/assigned-to/{userId}")
    public ResponseEntity<List<MaintenanceScheduleDTO>> getSchedulesByAssignedTo(
            @PathVariable Long userId) {
        List<MaintenanceSchedule> schedules = maintenanceScheduleService.findByAssignedToId(userId);
        return ResponseEntity.ok(schedules.stream()
                .map(this::convertToDTO)
                .toList());
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<MaintenanceScheduleDTO>> getSchedulesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<MaintenanceSchedule> schedules = maintenanceScheduleService.findByDateRange(start, end);
        return ResponseEntity.ok(schedules.stream()
                .map(this::convertToDTO)
                .toList());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<MaintenanceScheduleDTO> updateScheduleStatus(
            @PathVariable Long id,
            @RequestParam ScheduleStatus status) {
        maintenanceScheduleService.updateStatus(id, status);
        MaintenanceSchedule schedule = maintenanceScheduleService.findById(id);
        return ResponseEntity.ok(convertToDTO(schedule));
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<MaintenanceScheduleDTO> assignSchedule(
            @PathVariable Long id,
            @RequestParam Long userId) {
        maintenanceScheduleService.assignSchedule(id, userId);
        MaintenanceSchedule schedule = maintenanceScheduleService.findById(id);
        return ResponseEntity.ok(convertToDTO(schedule));
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<MaintenanceScheduleDTO> reschedule(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDate) {
        maintenanceScheduleService.reschedule(id, newDate);
        MaintenanceSchedule schedule = maintenanceScheduleService.findById(id);
        return ResponseEntity.ok(convertToDTO(schedule));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<MaintenanceScheduleDTO>> getOverdueSchedules() {
        List<MaintenanceSchedule> schedules = maintenanceScheduleService.findOverdueSchedules();
        return ResponseEntity.ok(schedules.stream()
                .map(this::convertToDTO)
                .toList());
    }

    private MaintenanceScheduleDTO convertToDTO(MaintenanceSchedule schedule) {
        MaintenanceScheduleDTO dto = new MaintenanceScheduleDTO();
        dto.setId(schedule.getId());
        dto.setAsset(convertToAssetDTO(schedule.getAsset()));
        dto.setMaintenanceType(schedule.getMaintenanceType());
        dto.setScheduledDate(schedule.getScheduledDate());
        dto.setStatus(schedule.getStatus());
        dto.setAssignedTo(convertToUserDTO(schedule.getAssignedTo()));
        dto.setRecurringPattern(schedule.getRecurringPattern());
        dto.setNextOccurrence(schedule.getNextOccurrence());
        dto.setEstimatedDurationMinutes(schedule.getEstimatedDurationMinutes());
        dto.setNotes(schedule.getNotes());
        dto.setCreatedAt(schedule.getCreatedAt());
        dto.setUpdatedAt(schedule.getUpdatedAt());
        return dto;
    }

    private AssetDTO convertToAssetDTO(Asset asset) {
        if (asset == null) {
            return null;
        }
        AssetDTO dto = new AssetDTO();
        dto.setId(asset.getId());
        dto.setName(asset.getName());
        dto.setType(asset.getType());
        dto.setStatus(asset.getStatus());
        return dto;
    }

    private UserDTO convertToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
} 