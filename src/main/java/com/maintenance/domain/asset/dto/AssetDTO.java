package com.maintenance.domain.asset.dto;

import com.maintenance.domain.asset.AssetCategory;
import com.maintenance.domain.asset.AssetStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AssetDTO {
    private Long id;
    private String name;
    private AssetCategory category;
    private AssetStatus status;
    private String location;
    private LocalDate purchaseDate;
    private LocalDate warrantyEndDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 