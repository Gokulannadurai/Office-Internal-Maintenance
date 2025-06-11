package com.maintenance.domain.maintenance;

import com.maintenance.domain.asset.Asset;
import com.maintenance.domain.asset.AssetService;
import com.maintenance.domain.maintenance.dto.CreateMaintenanceRequestRequest;
import com.maintenance.domain.maintenance.dto.MaintenanceRequestDTO;
import com.maintenance.domain.user.User;
import com.maintenance.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maintenance-requests")
@RequiredArgsConstructor
@Tag(name = "Maintenance Request Management", description = "APIs for managing maintenance requests")
public class MaintenanceRequestController {

    private final MaintenanceRequestService maintenanceRequestService;
    private final UserService userService;
    private final AssetService assetService;

    @PostMapping
    @Operation(summary = "Create a new maintenance request")
    public ResponseEntity<MaintenanceRequestDTO> createRequest(
            @Valid @RequestBody CreateMaintenanceRequestRequest request,
            @RequestParam Long requesterId) {
        
        User requester = userService.findById(requesterId);
        Asset asset = assetService.findById(request.getAssetId());
        
        MaintenanceRequest maintenanceRequest = MaintenanceRequest.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(RequestStatus.PENDING)
                .requester(requester)
                .asset(asset)
                .build();
        
        MaintenanceRequest createdRequest = maintenanceRequestService.create(maintenanceRequest);
        return new ResponseEntity<>(mapToDTO(createdRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get maintenance request by ID")
    public ResponseEntity<MaintenanceRequestDTO> getRequestById(@PathVariable Long id) {
        MaintenanceRequest request = maintenanceRequestService.findById(id);
        return ResponseEntity.ok(mapToDTO(request));
    }

    @GetMapping
    @Operation(summary = "Get all maintenance requests")
    public ResponseEntity<List<MaintenanceRequestDTO>> getAllRequests(
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Long requesterId,
            @RequestParam(required = false) Long assignedToId,
            @RequestParam(required = false) Long assetId) {
        
        List<MaintenanceRequest> requests;
        if (status != null && priority != null) {
            requests = maintenanceRequestService.findByStatusAndPriority(status, priority);
        } else if (status != null) {
            requests = maintenanceRequestService.findByStatus(status);
        } else if (priority != null) {
            requests = maintenanceRequestService.findByPriority(priority);
        } else if (requesterId != null) {
            requests = maintenanceRequestService.findByRequesterId(requesterId);
        } else if (assignedToId != null) {
            requests = maintenanceRequestService.findByAssignedToId(assignedToId);
        } else if (assetId != null) {
            requests = maintenanceRequestService.findByAssetId(assetId);
        } else {
            requests = maintenanceRequestService.findAll();
        }
        
        List<MaintenanceRequestDTO> requestDTOs = requests.stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(requestDTOs);
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "Assign maintenance request to a user")
    public ResponseEntity<Void> assignRequest(
            @PathVariable Long id,
            @RequestParam Long userId) {
        maintenanceRequestService.assignRequest(id, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update maintenance request status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam RequestStatus status) {
        maintenanceRequestService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/priority")
    @Operation(summary = "Update maintenance request priority")
    public ResponseEntity<Void> updatePriority(
            @PathVariable Long id,
            @RequestParam Priority priority) {
        maintenanceRequestService.updatePriority(id, priority);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending")
    @Operation(summary = "Get all pending maintenance requests")
    public ResponseEntity<List<MaintenanceRequestDTO>> getPendingRequests() {
        List<MaintenanceRequest> requests = maintenanceRequestService.findPendingRequests();
        List<MaintenanceRequestDTO> requestDTOs = requests.stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(requestDTOs);
    }

    @GetMapping("/assigned/{userId}")
    @Operation(summary = "Get all assigned maintenance requests for a user")
    public ResponseEntity<List<MaintenanceRequestDTO>> getAssignedRequests(@PathVariable Long userId) {
        List<MaintenanceRequest> requests = maintenanceRequestService.findAssignedRequests(userId);
        List<MaintenanceRequestDTO> requestDTOs = requests.stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(requestDTOs);
    }

    private MaintenanceRequestDTO mapToDTO(MaintenanceRequest request) {
        MaintenanceRequestDTO dto = new MaintenanceRequestDTO();
        dto.setId(request.getId());
        dto.setTitle(request.getTitle());
        dto.setDescription(request.getDescription());
        dto.setPriority(request.getPriority());
        dto.setStatus(request.getStatus());
        dto.setRequester(mapUserToDTO(request.getRequester()));
        dto.setAssignedTo(request.getAssignedTo() != null ? mapUserToDTO(request.getAssignedTo()) : null);
        dto.setAsset(mapAssetToDTO(request.getAsset()));
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }

    private UserDTO mapUserToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        return dto;
    }

    private AssetDTO mapAssetToDTO(Asset asset) {
        AssetDTO dto = new AssetDTO();
        dto.setId(asset.getId());
        dto.setName(asset.getName());
        dto.setCategory(asset.getCategory());
        dto.setStatus(asset.getStatus());
        dto.setLocation(asset.getLocation());
        dto.setPurchaseDate(asset.getPurchaseDate());
        dto.setWarrantyEndDate(asset.getWarrantyEndDate());
        return dto;
    }
} 