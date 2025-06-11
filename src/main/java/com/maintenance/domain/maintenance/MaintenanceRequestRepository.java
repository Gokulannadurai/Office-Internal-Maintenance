package com.maintenance.domain.maintenance;

import com.maintenance.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRequestRepository extends BaseRepository<MaintenanceRequest, Long> {
    List<MaintenanceRequest> findByStatus(RequestStatus status);
    List<MaintenanceRequest> findByPriority(Priority priority);
    List<MaintenanceRequest> findByRequesterId(Long requesterId);
    List<MaintenanceRequest> findByAssignedToId(Long assignedToId);
    List<MaintenanceRequest> findByAssetId(Long assetId);
    List<MaintenanceRequest> findByStatusAndPriority(RequestStatus status, Priority priority);
    List<MaintenanceRequest> findByRequesterIdAndStatus(Long requesterId, RequestStatus status);
    List<MaintenanceRequest> findByAssignedToIdAndStatus(Long assignedToId, RequestStatus status);
} 