package com.maintenance.domain.asset;

import com.maintenance.common.AbstractBaseService;
import com.maintenance.domain.maintenance.MaintenanceRequest;
import com.maintenance.domain.maintenance.MaintenanceRequestRepository;
import com.maintenance.exception.BusinessException;
import com.maintenance.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssetServiceImpl extends AbstractBaseService<Asset, Long, AssetRepository> implements AssetService {

    private final MaintenanceRequestRepository maintenanceRequestRepository;

    public AssetServiceImpl(AssetRepository repository, MaintenanceRequestRepository maintenanceRequestRepository) {
        super(repository);
        this.maintenanceRequestRepository = maintenanceRequestRepository;
    }

    @Override
    public List<Asset> findByCategory(AssetCategory category) {
        return repository.findByCategory(category);
    }

    @Override
    public List<Asset> findByStatus(AssetStatus status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<Asset> findByLocation(String location) {
        return repository.findByLocation(location);
    }

    @Override
    public List<Asset> findByStatusAndCategory(AssetStatus status, AssetCategory category) {
        return repository.findByStatusAndCategory(status, category);
    }

    @Override
    @Transactional
    public void updateStatus(Long assetId, AssetStatus status) {
        Asset asset = findById(assetId);
        asset.setStatus(status);
        repository.save(asset);
    }

    @Override
    public List<MaintenanceRequest> getMaintenanceHistory(Long assetId) {
        if (!repository.existsById(assetId)) {
            throw new NotFoundException("Asset not found with id: " + assetId);
        }
        return maintenanceRequestRepository.findByAssetId(assetId);
    }

    @Override
    public boolean isAssetAvailableForMaintenance(Long assetId) {
        Asset asset = findById(assetId);
        return asset.getStatus() != AssetStatus.UNDER_MAINTENANCE 
            && asset.getStatus() != AssetStatus.DISPOSED;
    }

    @Override
    @Transactional
    public Asset create(Asset asset) {
        validateAsset(asset);
        return super.create(asset);
    }

    @Override
    @Transactional
    public Asset update(Asset asset) {
        validateAsset(asset);
        return super.update(asset);
    }

    private void validateAsset(Asset asset) {
        if (asset.getPurchaseDate() != null && asset.getWarrantyEndDate() != null 
            && asset.getWarrantyEndDate().isBefore(asset.getPurchaseDate())) {
            throw new BusinessException("Warranty end date cannot be before purchase date", "INVALID_WARRANTY_DATE");
        }
    }
} 