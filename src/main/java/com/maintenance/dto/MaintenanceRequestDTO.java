package com.maintenance.dto;

import com.maintenance.entity.MaintenanceRequest;
import com.maintenance.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaintenanceRequestDTO {
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;
    
    @NotNull(message = "Category is required")
    private String category;
    
    @NotNull(message = "Priority is required")
    private String priority;
    
    private String status;
    private Long requesterId;
    private Long assignedToId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    
    public static MaintenanceRequestDTO fromEntity(MaintenanceRequest request) {
        MaintenanceRequestDTO dto = new MaintenanceRequestDTO();
        dto.setId(request.getId());
        dto.setTitle(request.getTitle());
        dto.setDescription(request.getDescription());
        dto.setCategory(request.getCategory().name());
        dto.setPriority(request.getPriority().name());
        dto.setStatus(request.getStatus().name());
        dto.setRequesterId(request.getRequester().getId());
        if (request.getAssignedTo() != null) {
            dto.setAssignedToId(request.getAssignedTo().getId());
        }
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        //dto.setCompletedAt(request.getCompletedAt());
        return dto;
    }
    
    public MaintenanceRequest toEntity() {
        MaintenanceRequest request = new MaintenanceRequest();
        request.setTitle(this.title);
        request.setDescription(this.description);
        request.setCategory(MaintenanceRequest.RequestCategory.valueOf(this.category));
        request.setPriority(MaintenanceRequest.RequestPriority.valueOf(this.priority));
        request.setStatus(MaintenanceRequest.RequestStatus.valueOf(this.status));
        
        User requester = new User();
        requester.setId(this.requesterId);
        request.setRequester(requester);
        
        if (this.assignedToId != null) {
            User assignedTo = new User();
            assignedTo.setId(this.assignedToId);
            request.setAssignedTo(assignedTo);
        }
        
        return request;
    }
} 