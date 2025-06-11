package com.maintenance.common;

import java.time.LocalDateTime;

public interface BaseEntity {
    Long getId();
    void setId(Long id);
    LocalDateTime getCreatedAt();
    void setCreatedAt(LocalDateTime createdAt);
    LocalDateTime getUpdatedAt();
    void setUpdatedAt(LocalDateTime updatedAt);
} 