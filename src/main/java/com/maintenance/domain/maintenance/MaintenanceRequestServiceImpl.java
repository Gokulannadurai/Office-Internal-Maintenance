package com.maintenance.domain.maintenance;

import com.maintenance.common.AbstractBaseService;
import com.maintenance.domain.asset.Asset;
import com.maintenance.domain.asset.AssetService;
import com.maintenance.domain.user.User;
import com.maintenance.domain.user.UserService;
import com.maintenance.exception.BusinessException;
import com.maintenance.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaintenanceRequestServiceImpl extends AbstractBaseService<MaintenanceRequest, Long, MaintenanceRequestRepository> 
        implements MaintenanceRequestService {

    private final UserService userService;
    private final AssetService assetService;

    public MaintenanceRequestServiceImpl(
            MaintenanceRequestRepository repository,
            UserService userService,
            AssetService assetService) {
        super(repository);
        this.userService = userService;
        this.assetService = assetService;
    }

    @Override
    public List<MaintenanceRequest> findByStatus(RequestStatus status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<MaintenanceRequest> findByPriority(Priority priority) {
        return repository.findByPriority(priority);
    }

    @Override
    public List<MaintenanceRequest> findByRequesterId(Long requesterId) {
        return repository.findByRequesterId(requesterId);
    }

    @Override
    public List<MaintenanceRequest> findByAssignedToId(Long assignedToId) {
        return repository.findByAssignedToId(assignedToId);
    }

    @Override
    public List<MaintenanceRequest> findByAssetId(Long assetId) {
        return repository.findByAssetId(assetId);
    }

    @Override
    @Transactional
    public void assignRequest(Long requestId, Long userId) {
        MaintenanceRequest request = findById(requestId);
        User user = userService.findById(userId);
        
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new BusinessException("Can only assign pending requests", "INVALID_REQUEST_STATUS");
        }
        
        request.setAssignedTo(user);
        request.setStatus(RequestStatus.ASSIGNED);
        repository.save(request);
    }

    @Override
    @Transactional
    public void updateStatus(Long requestId, RequestStatus status) {
        MaintenanceRequest request = findById(requestId);
        validateStatusTransition(request.getStatus(), status);
        
        request.setStatus(status);
        if (status == RequestStatus.IN_PROGRESS) {
            assetService.updateStatus(request.getAsset().getId(), AssetStatus.UNDER_MAINTENANCE);
        } else if (status == RequestStatus.COMPLETED) {
            assetService.updateStatus(request.getAsset().getId(), AssetStatus.OPERATIONAL);
        }
        
        repository.save(request);
    }

    @Override
    @Transactional
    public void updatePriority(Long requestId, Priority priority) {
        MaintenanceRequest request = findById(requestId);
        request.setPriority(priority);
        repository.save(request);
    }

    @Override
    public List<MaintenanceRequest> findPendingRequests() {
        return repository.findByStatus(RequestStatus.PENDING);
    }

    @Override
    public List<MaintenanceRequest> findAssignedRequests(Long userId) {
        return repository.findByAssignedToIdAndStatus(userId, RequestStatus.ASSIGNED);
    }

    @Override
    @Transactional
    public MaintenanceRequest create(MaintenanceRequest request) {
        validateRequest(request);
        return super.create(request);
    }

    private void validateRequest(MaintenanceRequest request) {
        if (!assetService.isAssetAvailableForMaintenance(request.getAsset().getId())) {
            throw new BusinessException("Asset is not available for maintenance", "ASSET_UNAVAILABLE");
        }
    }

    private void validateStatusTransition(RequestStatus currentStatus, RequestStatus newStatus) {
        if (currentStatus == RequestStatus.COMPLETED || currentStatus == RequestStatus.CANCELLED) {
            throw new BusinessException("Cannot change status of completed or cancelled requests", "INVALID_STATUS_CHANGE");
        }
        
        if (currentStatus == RequestStatus.PENDING && newStatus != RequestStatus.ASSIGNED && newStatus != RequestStatus.CANCELLED) {
            throw new BusinessException("Pending requests can only be assigned or cancelled", "INVALID_STATUS_CHANGE");
        }
        
        if (currentStatus == RequestStatus.ASSIGNED && newStatus != RequestStatus.IN_PROGRESS && newStatus != RequestStatus.CANCELLED) {
            throw new BusinessException("Assigned requests can only be started or cancelled", "INVALID_STATUS_CHANGE");
        }
    }
} 