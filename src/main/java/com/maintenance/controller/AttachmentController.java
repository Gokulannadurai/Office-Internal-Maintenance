package com.maintenance.controller;

import com.maintenance.dto.ApiResponse;
import com.maintenance.dto.AttachmentDTO;
import com.maintenance.entity.Attachment;
import com.maintenance.service.AttachmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<AttachmentDTO>> createAttachment(
            @Valid @ModelAttribute AttachmentDTO attachmentDTO,
            @RequestParam("file") MultipartFile file) {
        Attachment attachment = attachmentService.createAttachment(attachmentDTO.toEntity(), file);
        return ResponseEntity.ok(ApiResponse.success("Attachment created successfully", 
            AttachmentDTO.fromEntity(attachment)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAttachment(@PathVariable Long id) {
        attachmentService.deleteAttachment(id);
        return ResponseEntity.ok(ApiResponse.success("Attachment deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AttachmentDTO>> getAttachmentById(@PathVariable Long id) {
        Attachment attachment = attachmentService.getAttachmentById(id);
        return ResponseEntity.ok(ApiResponse.success(AttachmentDTO.fromEntity(attachment)));
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<ApiResponse<Page<AttachmentDTO>>> getAttachmentsByRequest(
            @PathVariable Long requestId,
            Pageable pageable) {
        Page<Attachment> attachments = attachmentService.getAttachmentsByRequest(requestId, pageable);
        Page<AttachmentDTO> attachmentDTOs = attachments.map(AttachmentDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(attachmentDTOs));
    }

    @GetMapping("/request/{requestId}/search")
    public ResponseEntity<ApiResponse<Page<AttachmentDTO>>> searchAttachmentsInRequest(
            @PathVariable Long requestId,
            @RequestParam String searchTerm,
            Pageable pageable) {
        Page<Attachment> attachments = attachmentService.searchAttachmentsInRequest(requestId, searchTerm, pageable);
        Page<AttachmentDTO> attachmentDTOs = attachments.map(AttachmentDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(attachmentDTOs));
    }

    @GetMapping("/request/{requestId}/type/{fileType}")
    public ResponseEntity<ApiResponse<Page<AttachmentDTO>>> getAttachmentsByFileType(
            @PathVariable Long requestId,
            @PathVariable String fileType,
            Pageable pageable) {
        Page<Attachment> attachments = attachmentService.getAttachmentsByFileType(requestId, fileType, pageable);
        Page<AttachmentDTO> attachmentDTOs = attachments.map(AttachmentDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(attachmentDTOs));
    }

    @GetMapping("/request/{requestId}/largest")
    public ResponseEntity<ApiResponse<Page<AttachmentDTO>>> getLargestAttachments(
            @PathVariable Long requestId,
            Pageable pageable) {
        Page<Attachment> attachments = attachmentService.getLargestAttachments(requestId, pageable);
        Page<AttachmentDTO> attachmentDTOs = attachments.map(AttachmentDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(attachmentDTOs));
    }

    @GetMapping("/request/{requestId}/size")
    public ResponseEntity<ApiResponse<Long>> getTotalSizeByRequest(@PathVariable Long requestId) {
        Long totalSize = attachmentService.getTotalSizeByRequest(requestId);
        return ResponseEntity.ok(ApiResponse.success(totalSize));
    }

    @GetMapping("/request/{requestId}/types")
    public ResponseEntity<ApiResponse<Page<AttachmentDTO>>> getAttachmentsByFileTypes(
            @PathVariable Long requestId,
            @RequestParam List<String> fileTypes,
            Pageable pageable) {
        Page<Attachment> attachments = attachmentService.getAttachmentsByFileTypes(requestId, fileTypes, pageable);
        Page<AttachmentDTO> attachmentDTOs = attachments.map(AttachmentDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(attachmentDTOs));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long id) {
        Attachment attachment = attachmentService.getAttachmentById(id);
        byte[] fileContent = attachmentService.downloadAttachment(id);
        
        ByteArrayResource resource = new ByteArrayResource(fileContent);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .contentLength(fileContent.length)
                .body(resource);
    }
} 