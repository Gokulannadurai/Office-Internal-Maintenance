package com.maintenance.service;

import com.maintenance.entity.Attachment;
import com.maintenance.entity.MaintenanceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AttachmentService {
    Attachment createAttachment(Attachment attachment, MultipartFile file);
    void deleteAttachment(Long id);
    Optional<Attachment> getAttachmentById(Long id);
    Page<Attachment> getAttachmentsByRequest(MaintenanceRequest request, Pageable pageable);
    Page<Attachment> searchAttachmentsInRequest(MaintenanceRequest request, String searchTerm, Pageable pageable);
    Page<Attachment> getAttachmentsByFileType(MaintenanceRequest request, String fileType, Pageable pageable);
    Page<Attachment> getLargestAttachments(MaintenanceRequest request, Pageable pageable);
    Long getTotalSizeByRequest(MaintenanceRequest request);
    Page<Attachment> getAttachmentsByFileTypes(MaintenanceRequest request, List<String> fileTypes, Pageable pageable);
    byte[] downloadAttachment(Long id);
} 