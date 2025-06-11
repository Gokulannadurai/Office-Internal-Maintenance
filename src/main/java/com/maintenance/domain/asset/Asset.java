package com.maintenance.domain.asset;

import com.maintenance.common.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "assets")
@Getter
@Setter
@SuperBuilder
public class Asset extends AbstractBaseEntity {
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetCategory category;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetStatus status;
    
    @Column(length = 100)
    private String location;
    
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;
    
    @Column(name = "warranty_end_date")
    private LocalDate warrantyEndDate;
    
    public Asset() {
        // Default constructor required by JPA
    }
} 