package com.maintenance.service;

import com.maintenance.entity.MaintenanceRequest;
import com.maintenance.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public interface MaintenanceRequestService {
    MaintenanceRequest createRequest(MaintenanceRequest request);
    MaintenanceRequest updateRequest(Long id, MaintenanceRequest request);
    void deleteRequest(Long id);
    Optional<MaintenanceRequest> getRequestById(Long id);
    Page<MaintenanceRequest> getAllRequests(Pageable pageable);
    Page<MaintenanceRequest> getRequestsByRequester(User requester, Pageable pageable);
    Page<MaintenanceRequest> getRequestsByAssignedTo(User assignedTo, Pageable pageable);
    Page<MaintenanceRequest> searchRequests(String searchTerm, Pageable pageable);
    Page<MaintenanceRequest> getRequestsByStatus(MaintenanceRequest.RequestStatus status, Pageable pageable);
    Page<MaintenanceRequest> getRequestsByPriority(MaintenanceRequest.RequestPriority priority, Pageable pageable);
    Page<MaintenanceRequest> getRequestsByCategory(MaintenanceRequest.RequestCategory category, Pageable pageable);
    Page<MaintenanceRequest> getRequestsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<MaintenanceRequest> getUnassignedRequests(Pageable pageable);
    Page<MaintenanceRequest> getUserRelatedRequests(User user, Pageable pageable);
    void assignRequest(Long requestId, Long userId);
    void updateRequestStatus(Long requestId, MaintenanceRequest.RequestStatus status);
    void updateRequestPriority(Long requestId, MaintenanceRequest.RequestPriority priority);
    Map<MaintenanceRequest.RequestStatus, Long> getRequestStatusCounts();
} 