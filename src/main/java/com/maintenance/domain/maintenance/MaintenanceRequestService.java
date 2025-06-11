package com.maintenance.domain.maintenance;

import com.maintenance.common.BaseService;

import java.util.List;

public interface MaintenanceRequestService extends BaseService<MaintenanceRequest, Long> {
    List<MaintenanceRequest> findByStatus(RequestStatus status);
    List<MaintenanceRequest> findByPriority(Priority priority);
    List<MaintenanceRequest> findByRequesterId(Long requesterId);
    List<MaintenanceRequest> findByAssignedToId(Long assignedToId);
    List<MaintenanceRequest> findByAssetId(Long assetId);
    void assignRequest(Long requestId, Long userId);
    void updateStatus(Long requestId, RequestStatus status);
    void updatePriority(Long requestId, Priority priority);
    List<MaintenanceRequest> findPendingRequests();
    List<MaintenanceRequest> findAssignedRequests(Long userId);
} 