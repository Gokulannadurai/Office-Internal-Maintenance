package com.maintenance.domain.asset.dto;

import com.maintenance.domain.asset.AssetCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateAssetRequest {
    @NotBlank(message = "Asset name is required")
    private String name;

    @NotNull(message = "Asset category is required")
    private AssetCategory category;

    private String location;
    private LocalDate purchaseDate;
    private LocalDate warrantyEndDate;
} 