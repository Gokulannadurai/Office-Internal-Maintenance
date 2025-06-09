package com.maintenance.service;

import com.maintenance.entity.Attachment;
import com.maintenance.entity.MaintenanceRequest;
import com.maintenance.repository.AttachmentRepository;
import com.maintenance.repository.MaintenanceRequestRepository;
import com.maintenance.service.impl.AttachmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AttachmentServiceTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private MaintenanceRequestRepository maintenanceRequestRepository;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @TempDir
    Path tempDir;

    private Attachment testAttachment;
    private MaintenanceRequest testRequest;
    private MockMultipartFile testFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(attachmentService, "uploadDir", tempDir.toString());

        testRequest = new MaintenanceRequest();
        testRequest.setId(1L);
        testRequest.setTitle("Test Request");

        testAttachment = new Attachment();
        testAttachment.setId(1L);
        testAttachment.setFileName("test.jpg");
        testAttachment.setFileType("image/jpeg");
        testAttachment.setFileSize(1024L);
        testAttachment.setMaintenanceRequest(testRequest);

        testFile = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );
    }

    @Test
    void createAttachment_Success() throws IOException {
        when(attachmentRepository.save(any(Attachment.class))).thenReturn(testAttachment);

        Attachment createdAttachment = attachmentService.createAttachment(testAttachment, testFile);

        assertNotNull(createdAttachment);
        assertEquals(testAttachment.getFileName(), createdAttachment.getFileName());
        verify(attachmentRepository).save(any(Attachment.class));
    }

    @Test
    void createAttachment_NullRequest() {
        testAttachment.setMaintenanceRequest(null);

        assertThrows(RuntimeException.class, () -> 
            attachmentService.createAttachment(testAttachment, testFile));
    }

    @Test
    void createAttachment_EmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            new byte[0]
        );

        assertThrows(RuntimeException.class, () -> 
            attachmentService.createAttachment(testAttachment, emptyFile));
    }

    @Test
    void deleteAttachment_Success() {
        when(attachmentRepository.findById(anyLong())).thenReturn(Optional.of(testAttachment));

        attachmentService.deleteAttachment(1L);

        verify(attachmentRepository).delete(any(Attachment.class));
    }

    @Test
    void deleteAttachment_NotFound() {
        when(attachmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> attachmentService.deleteAttachment(1L));
    }

    @Test
    void getAttachmentsByRequest_Success() {
        Page<Attachment> attachmentPage = new PageImpl<>(Arrays.asList(testAttachment));
        when(attachmentRepository.findByMaintenanceRequest(any(MaintenanceRequest.class), any(PageRequest.class)))
                .thenReturn(attachmentPage);

        Page<Attachment> foundAttachments = attachmentService.getAttachmentsByRequest(testRequest, PageRequest.of(0, 10));

        assertNotNull(foundAttachments);
        assertEquals(1, foundAttachments.getTotalElements());
    }

    @Test
    void getAttachmentsByFileType_Success() {
        Page<Attachment> attachmentPage = new PageImpl<>(Arrays.asList(testAttachment));
        when(attachmentRepository.findByFileType(any(MaintenanceRequest.class), anyString(), any(PageRequest.class)))
                .thenReturn(attachmentPage);

        Page<Attachment> foundAttachments = attachmentService.getAttachmentsByFileType(testRequest, "image/jpeg", PageRequest.of(0, 10));

        assertNotNull(foundAttachments);
        assertEquals(1, foundAttachments.getTotalElements());
    }

    @Test
    void getLargestAttachments_Success() {
        Page<Attachment> attachmentPage = new PageImpl<>(Arrays.asList(testAttachment));
        when(attachmentRepository.findLargestAttachments(any(MaintenanceRequest.class), any(PageRequest.class)))
                .thenReturn(attachmentPage);

        Page<Attachment> foundAttachments = attachmentService.getLargestAttachments(testRequest, PageRequest.of(0, 10));

        assertNotNull(foundAttachments);
        assertEquals(1, foundAttachments.getTotalElements());
    }

    @Test
    void getTotalSizeByRequest_Success() {
        when(attachmentRepository.getTotalSizeByRequest(any(MaintenanceRequest.class))).thenReturn(1024L);

        Long totalSize = attachmentService.getTotalSizeByRequest(testRequest);

        assertEquals(1024L, totalSize);
    }

    @Test
    void getAttachmentsByFileTypes_Success() {
        Page<Attachment> attachmentPage = new PageImpl<>(Arrays.asList(testAttachment));
        List<String> fileTypes = Arrays.asList("image/jpeg", "image/png");
        when(attachmentRepository.findByFileTypes(any(MaintenanceRequest.class), anyList(), any(PageRequest.class)))
                .thenReturn(attachmentPage);

        Page<Attachment> foundAttachments = attachmentService.getAttachmentsByFileTypes(testRequest, fileTypes, PageRequest.of(0, 10));

        assertNotNull(foundAttachments);
        assertEquals(1, foundAttachments.getTotalElements());
    }

    @Test
    void downloadAttachment_Success() throws IOException {
        when(attachmentRepository.findById(anyLong())).thenReturn(Optional.of(testAttachment));

        byte[] content = attachmentService.downloadAttachment(1L);

        assertNotNull(content);
    }

    @Test
    void downloadAttachment_NotFound() {
        when(attachmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> attachmentService.downloadAttachment(1L));
    }
} 