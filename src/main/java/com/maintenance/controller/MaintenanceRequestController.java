package com.maintenance.controller;

import com.maintenance.dto.ApiResponse;
import com.maintenance.dto.MaintenanceRequestDTO;
import com.maintenance.entity.MaintenanceRequest;
import com.maintenance.service.MaintenanceRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/maintenance-requests")
public class MaintenanceRequestController {

    @Autowired
    private MaintenanceRequestService maintenanceRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<MaintenanceRequestDTO>> createRequest(
            @Valid @RequestBody MaintenanceRequestDTO requestDTO) {
        MaintenanceRequest request = maintenanceRequestService.createRequest(requestDTO.toEntity());
        return ResponseEntity.ok(ApiResponse.success("Maintenance request created successfully", 
            MaintenanceRequestDTO.fromEntity(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MaintenanceRequestDTO>> updateRequest(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceRequestDTO requestDTO) {
        MaintenanceRequest request = maintenanceRequestService.updateRequest(id, requestDTO.toEntity());
        return ResponseEntity.ok(ApiResponse.success("Maintenance request updated successfully", 
            MaintenanceRequestDTO.fromEntity(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRequest(@PathVariable Long id) {
        maintenanceRequestService.deleteRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Maintenance request deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MaintenanceRequestDTO>> getRequestById(@PathVariable Long id) {
        MaintenanceRequest request = maintenanceRequestService.getRequestById(id);
        return ResponseEntity.ok(ApiResponse.success(MaintenanceRequestDTO.fromEntity(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MaintenanceRequestDTO>>> getAllRequests(Pageable pageable) {
        Page<MaintenanceRequest> requests = maintenanceRequestService.getAllRequests(pageable);
        Page<MaintenanceRequestDTO> requestDTOs = requests.map(MaintenanceRequestDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(requestDTOs));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<MaintenanceRequestDTO>>> searchRequests(
            @RequestParam String searchTerm,
            Pageable pageable) {
        Page<MaintenanceRequest> requests = maintenanceRequestService.searchRequests(searchTerm, pageable);
        Page<MaintenanceRequestDTO> requestDTOs = requests.map(MaintenanceRequestDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(requestDTOs));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<MaintenanceRequestDTO>>> filterRequests(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        Page<MaintenanceRequest> requests = maintenanceRequestService.filterRequests(
            status, priority, category, startDate, endDate, pageable);
        Page<MaintenanceRequestDTO> requestDTOs = requests.map(MaintenanceRequestDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(requestDTOs));
    }

    @GetMapping("/requester/{requesterId}")
    public ResponseEntity<ApiResponse<Page<MaintenanceRequestDTO>>> getRequestsByRequester(
            @PathVariable Long requesterId,
            Pageable pageable) {
        Page<MaintenanceRequest> requests = maintenanceRequestService.getRequestsByRequester(requesterId, pageable);
        Page<MaintenanceRequestDTO> requestDTOs = requests.map(MaintenanceRequestDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(requestDTOs));
    }

    @GetMapping("/assigned/{userId}")
    public ResponseEntity<ApiResponse<Page<MaintenanceRequestDTO>>> getRequestsByAssignedUser(
            @PathVariable Long userId,
            Pageable pageable) {
        Page<MaintenanceRequest> requests = maintenanceRequestService.getRequestsByAssignedUser(userId, pageable);
        Page<MaintenanceRequestDTO> requestDTOs = requests.map(MaintenanceRequestDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(requestDTOs));
    }

    @GetMapping("/status/count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getRequestCountByStatus() {
        Map<String, Long> statusCount = maintenanceRequestService.getRequestCountByStatus();
        return ResponseEntity.ok(ApiResponse.success(statusCount));
    }

    @GetMapping("/unassigned")
    public ResponseEntity<ApiResponse<Page<MaintenanceRequestDTO>>> getUnassignedRequests(Pageable pageable) {
        Page<MaintenanceRequest> requests = maintenanceRequestService.getUnassignedRequests(pageable);
        Page<MaintenanceRequestDTO> requestDTOs = requests.map(MaintenanceRequestDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(requestDTOs));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<MaintenanceRequestDTO>>> getRequestsByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        Page<MaintenanceRequest> requests = maintenanceRequestService.getRequestsByUser(userId, pageable);
        Page<MaintenanceRequestDTO> requestDTOs = requests.map(MaintenanceRequestDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(requestDTOs));
    }
} 