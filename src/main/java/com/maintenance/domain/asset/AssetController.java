package com.maintenance.domain.asset;

import com.maintenance.domain.asset.dto.AssetDTO;
import com.maintenance.domain.asset.dto.CreateAssetRequest;
import com.maintenance.domain.maintenance.dto.MaintenanceRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assets")
@RequiredArgsConstructor
@Tag(name = "Asset Management", description = "APIs for managing assets")
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    @Operation(summary = "Create a new asset")
    public ResponseEntity<AssetDTO> createAsset(@Valid @RequestBody CreateAssetRequest request) {
        Asset asset = Asset.builder()
                .name(request.getName())
                .category(request.getCategory())
                .status(AssetStatus.OPERATIONAL)
                .location(request.getLocation())
                .purchaseDate(request.getPurchaseDate())
                .warrantyEndDate(request.getWarrantyEndDate())
                .build();
        
        Asset createdAsset = assetService.create(asset);
        return new ResponseEntity<>(mapToDTO(createdAsset), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get asset by ID")
    public ResponseEntity<AssetDTO> getAssetById(@PathVariable Long id) {
        Asset asset = assetService.findById(id);
        return ResponseEntity.ok(mapToDTO(asset));
    }

    @GetMapping
    @Operation(summary = "Get all assets")
    public ResponseEntity<List<AssetDTO>> getAllAssets(
            @RequestParam(required = false) AssetCategory category,
            @RequestParam(required = false) AssetStatus status,
            @RequestParam(required = false) String location) {
        
        List<Asset> assets;
        if (category != null && status != null) {
            assets = assetService.findByStatusAndCategory(status, category);
        } else if (category != null) {
            assets = assetService.findByCategory(category);
        } else if (status != null) {
            assets = assetService.findByStatus(status);
        } else if (location != null) {
            assets = assetService.findByLocation(location);
        } else {
            assets = assetService.findAll();
        }
        
        List<AssetDTO> assetDTOs = assets.stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(assetDTOs);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update asset status")
    public ResponseEntity<Void> updateAssetStatus(
            @PathVariable Long id,
            @RequestParam AssetStatus status) {
        assetService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/maintenance-history")
    @Operation(summary = "Get asset maintenance history")
    public ResponseEntity<List<MaintenanceRequestDTO>> getMaintenanceHistory(@PathVariable Long id) {
        List<MaintenanceRequestDTO> history = assetService.getMaintenanceHistory(id).stream()
                .map(this::mapToMaintenanceDTO)
                .toList();
        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete asset")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private AssetDTO mapToDTO(Asset asset) {
        AssetDTO dto = new AssetDTO();
        dto.setId(asset.getId());
        dto.setName(asset.getName());
        dto.setCategory(asset.getCategory());
        dto.setStatus(asset.getStatus());
        dto.setLocation(asset.getLocation());
        dto.setPurchaseDate(asset.getPurchaseDate());
        dto.setWarrantyEndDate(asset.getWarrantyEndDate());
        dto.setCreatedAt(asset.getCreatedAt());
        dto.setUpdatedAt(asset.getUpdatedAt());
        return dto;
    }

    private MaintenanceRequestDTO mapToMaintenanceDTO(MaintenanceRequest request) {
        // This will be implemented when we create the MaintenanceRequest domain
        return null;
    }
} 