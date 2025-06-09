package com.maintenance.service.impl;

import com.maintenance.entity.Attachment;
import com.maintenance.entity.MaintenanceRequest;
import com.maintenance.repository.AttachmentRepository;
import com.maintenance.repository.MaintenanceRequestRepository;
import com.maintenance.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private MaintenanceRequestRepository maintenanceRequestRepository;

    @Override
    public Attachment createAttachment(Attachment attachment, MultipartFile file) {
        if (attachment.getMaintenanceRequest() == null) {
            throw new RuntimeException("Maintenance request cannot be null");
        }
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File cannot be empty");
        }

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            // Save file to disk
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            // Set attachment properties
            attachment.setFileName(originalFilename);
            attachment.setFileType(file.getContentType());
            attachment.setFileSize(file.getSize());
            attachment.setFilePath(filePath.toString());

            return attachmentRepository.save(attachment);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }

    @Override
    public void deleteAttachment(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        try {
            // Delete file from disk
            Path filePath = Paths.get(attachment.getFilePath());
            Files.deleteIfExists(filePath);

            // Delete from database
            attachmentRepository.delete(attachment);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Attachment> getAttachmentById(Long id) {
        return attachmentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attachment> getAttachmentsByRequest(MaintenanceRequest request, Pageable pageable) {
        return attachmentRepository.findByMaintenanceRequest(request, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attachment> searchAttachmentsInRequest(MaintenanceRequest request, String searchTerm, Pageable pageable) {
        return attachmentRepository.searchAttachmentsInRequest(request, searchTerm, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attachment> getAttachmentsByFileType(MaintenanceRequest request, String fileType, Pageable pageable) {
        return attachmentRepository.findByFileType(request, fileType, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attachment> getLargestAttachments(MaintenanceRequest request, Pageable pageable) {
        return attachmentRepository.findLargestAttachments(request, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalSizeByRequest(MaintenanceRequest request) {
        return attachmentRepository.getTotalSizeByRequest(request);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attachment> getAttachmentsByFileTypes(MaintenanceRequest request, List<String> fileTypes, Pageable pageable) {
        return attachmentRepository.findByFileTypes(request, fileTypes, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadAttachment(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        try {
            Path filePath = Paths.get(attachment.getFilePath());
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + e.getMessage());
        }
    }
} 