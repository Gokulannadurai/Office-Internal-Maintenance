package com.maintenance.domain.asset;

import com.maintenance.common.BaseService;
import com.maintenance.domain.maintenance.MaintenanceRequest;

import java.util.List;

public interface AssetService extends BaseService<Asset, Long> {
    List<Asset> findByCategory(AssetCategory category);
    List<Asset> findByStatus(AssetStatus status);
    List<Asset> findByLocation(String location);
    List<Asset> findByStatusAndCategory(AssetStatus status, AssetCategory category);
    void updateStatus(Long assetId, AssetStatus status);
    List<MaintenanceRequest> getMaintenanceHistory(Long assetId);
    boolean isAssetAvailableForMaintenance(Long assetId);
} 