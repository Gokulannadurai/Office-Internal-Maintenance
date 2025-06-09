package com.maintenance.controller;

import com.maintenance.dto.AttachmentDTO;
import com.maintenance.entity.Attachment;
import com.maintenance.entity.MaintenanceRequest;
import com.maintenance.service.AttachmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AttachmentControllerTest {

    private AttachmentController attachmentController;

    @Mock
    private AttachmentService attachmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        attachmentController = new AttachmentController(attachmentService);
    }

    @Test
    void uploadAttachment_ValidFile_ReturnsCreatedAttachment() {
        // Arrange
        Long requestId = 1L;
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.pdf",
            "application/pdf",
            "test content".getBytes()
        );

        Attachment createdAttachment = new Attachment();
        createdAttachment.setId(1L);
        createdAttachment.setFileName("test.pdf");
        createdAttachment.setFileType("application/pdf");
        createdAttachment.setFileSize(file.getSize());

        when(attachmentService.createAttachment(any(Attachment.class), any())).thenReturn(createdAttachment);

        // Act
        ResponseEntity<AttachmentDTO> response = attachmentController.uploadAttachment(requestId, file);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdAttachment.getId(), response.getBody().getId());
        assertEquals(createdAttachment.getFileName(), response.getBody().getFileName());
    }

    @Test
    void getAttachmentById_ExistingAttachment_ReturnsAttachment() {
        // Arrange
        Long attachmentId = 1L;
        Attachment attachment = new Attachment();
        attachment.setId(attachmentId);
        attachment.setFileName("test.pdf");
        attachment.setFileType("application/pdf");

        when(attachmentService.getAttachmentById(attachmentId)).thenReturn(Optional.of(attachment));

        // Act
        ResponseEntity<AttachmentDTO> response = attachmentController.getAttachmentById(attachmentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(attachmentId, response.getBody().getId());
        assertEquals(attachment.getFileName(), response.getBody().getFileName());
    }

    @Test
    void getAttachmentById_NonExistingAttachment_ReturnsNotFound() {
        // Arrange
        Long attachmentId = 999L;
        when(attachmentService.getAttachmentById(attachmentId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<AttachmentDTO> response = attachmentController.getAttachmentById(attachmentId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteAttachment_ExistingAttachment_ReturnsNoContent() {
        // Arrange
        Long attachmentId = 1L;
        doNothing().when(attachmentService).deleteAttachment(attachmentId);

        // Act
        ResponseEntity<Void> response = attachmentController.deleteAttachment(attachmentId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(attachmentService).deleteAttachment(attachmentId);
    }

    @Test
    void getAttachmentsByRequest_ReturnsAttachmentList() {
        // Arrange
        Long requestId = 1L;
        Attachment attachment1 = new Attachment();
        attachment1.setId(1L);
        attachment1.setFileName("file1.pdf");

        Attachment attachment2 = new Attachment();
        attachment2.setId(2L);
        attachment2.setFileName("file2.pdf");

        List<Attachment> attachments = Arrays.asList(attachment1, attachment2);
        Page<Attachment> attachmentPage = new PageImpl<>(attachments);
        Pageable pageable = PageRequest.of(0, 10);

        when(attachmentService.getAttachmentsByRequest(any(MaintenanceRequest.class), eq(pageable)))
                .thenReturn(attachmentPage);

        // Act
        ResponseEntity<Page<AttachmentDTO>> response = attachmentController.getAttachmentsByRequest(requestId, pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    void searchAttachmentsInRequest_ReturnsMatchingAttachments() {
        // Arrange
        Long requestId = 1L;
        String searchTerm = "test";
        Attachment attachment1 = new Attachment();
        attachment1.setId(1L);
        attachment1.setFileName("test1.pdf");

        Attachment attachment2 = new Attachment();
        attachment2.setId(2L);
        attachment2.setFileName("test2.pdf");

        List<Attachment> attachments = Arrays.asList(attachment1, attachment2);
        Page<Attachment> attachmentPage = new PageImpl<>(attachments);
        Pageable pageable = PageRequest.of(0, 10);

        when(attachmentService.searchAttachmentsInRequest(any(MaintenanceRequest.class), eq(searchTerm), eq(pageable)))
                .thenReturn(attachmentPage);

        // Act
        ResponseEntity<Page<AttachmentDTO>> response = attachmentController.searchAttachmentsInRequest(requestId, searchTerm, pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    void getAttachmentsByFileType_ReturnsFilteredAttachments() {
        // Arrange
        Long requestId = 1L;
        String fileType = "application/pdf";
        Attachment attachment1 = new Attachment();
        attachment1.setId(1L);
        attachment1.setFileType(fileType);

        Attachment attachment2 = new Attachment();
        attachment2.setId(2L);
        attachment2.setFileType(fileType);

        List<Attachment> attachments = Arrays.asList(attachment1, attachment2);
        Page<Attachment> attachmentPage = new PageImpl<>(attachments);
        Pageable pageable = PageRequest.of(0, 10);

        when(attachmentService.getAttachmentsByFileType(any(MaintenanceRequest.class), eq(fileType), eq(pageable)))
                .thenReturn(attachmentPage);

        // Act
        ResponseEntity<Page<AttachmentDTO>> response = attachmentController.getAttachmentsByFileType(requestId, fileType, pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    void downloadAttachment_ExistingAttachment_ReturnsFile() {
        // Arrange
        Long attachmentId = 1L;
        byte[] fileContent = "test content".getBytes();
        Resource resource = new ByteArrayResource(fileContent);

        when(attachmentService.downloadAttachment(attachmentId)).thenReturn(resource);

        // Act
        ResponseEntity<Resource> response = attachmentController.downloadAttachment(attachmentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ByteArrayResource);
    }

    @Test
    void getTotalSizeByRequest_ReturnsTotalSize() {
        // Arrange
        Long requestId = 1L;
        long expectedSize = 1024L;

        when(attachmentService.getTotalSizeByRequest(any(MaintenanceRequest.class))).thenReturn(expectedSize);

        // Act
        ResponseEntity<Long> response = attachmentController.getTotalSizeByRequest(requestId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedSize, response.getBody());
    }
} 