package com.maintenance.domain.asset;

import com.maintenance.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends BaseRepository<Asset, Long> {
    List<Asset> findByCategory(AssetCategory category);
    List<Asset> findByStatus(AssetStatus status);
    List<Asset> findByLocation(String location);
    List<Asset> findByStatusAndCategory(AssetStatus status, AssetCategory category);
} 