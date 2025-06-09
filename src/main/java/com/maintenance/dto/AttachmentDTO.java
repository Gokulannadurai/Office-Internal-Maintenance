package com.maintenance.dto;

import com.maintenance.entity.Attachment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachmentDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String filePath;
    private Long maintenanceRequestId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static AttachmentDTO fromEntity(Attachment attachment) {
        AttachmentDTO dto = new AttachmentDTO();
        dto.setId(attachment.getId());
        dto.setFileName(attachment.getFileName());
        dto.setFileType(attachment.getFileType());
        dto.setFileSize(attachment.getFileSize());
        dto.setFilePath(attachment.getFilePath());
        dto.setMaintenanceRequestId(attachment.getMaintenanceRequest().getId());
        dto.setCreatedAt(attachment.getCreatedAt());
        dto.setUpdatedAt(attachment.getUpdatedAt());
        return dto;
    }
    
    public Attachment toEntity() {
        Attachment attachment = new Attachment();
        attachment.setFileName(this.fileName);
        attachment.setFileType(this.fileType);
        attachment.setFileSize(this.fileSize);
        attachment.setFilePath(this.filePath);
        return attachment;
    }
} 