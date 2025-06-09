package com.maintenance.repository;

import com.maintenance.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MaintenanceRequestRepository maintenanceRequestRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Test
    void testUserRepository() {
        // Create and save a user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.getRoles().add("ROLE_USER");
        user = userRepository.save(user);

        // Test find by username
        assertTrue(userRepository.findByUsername("testuser").isPresent());
        
        // Test search users
        var pageable = PageRequest.of(0, 10);
        var searchResults = userRepository.searchUsers("test", pageable);
        assertTrue(searchResults.getTotalElements() > 0);
    }

    @Test
    void testMaintenanceRequestRepository() {
        // Create and save a user
        User user = new User();
        user.setUsername("requester");
        user.setPassword("password");
        user.setFullName("Test Requester");
        user.setEmail("requester@example.com");
        user = userRepository.save(user);

        // Create and save a maintenance request
        MaintenanceRequest request = new MaintenanceRequest();
        request.setTitle("Test Request");
        request.setDescription("Test Description");
        request.setCategory(MaintenanceRequest.RequestCategory.GENERAL);
        request.setPriority(MaintenanceRequest.RequestPriority.MEDIUM);
        request.setStatus(MaintenanceRequest.RequestStatus.OPEN);
        request.setRequester(user);
        request = maintenanceRequestRepository.save(request);

        // Test find by requester
        var pageable = PageRequest.of(0, 10);
        var requests = maintenanceRequestRepository.findByRequester(user, pageable);
        assertTrue(requests.getTotalElements() > 0);
    }

    @Test
    void testCommentRepository() {
        // Create and save a user
        User user = new User();
        user.setUsername("commenter");
        user.setPassword("password");
        user.setFullName("Test Commenter");
        user.setEmail("commenter@example.com");
        user = userRepository.save(user);

        // Create and save a maintenance request
        MaintenanceRequest request = new MaintenanceRequest();
        request.setTitle("Test Request");
        request.setDescription("Test Description");
        request.setCategory(MaintenanceRequest.RequestCategory.GENERAL);
        request.setRequester(user);
        request = maintenanceRequestRepository.save(request);

        // Create and save a comment
        Comment comment = new Comment();
        comment.setContent("Test Comment");
        comment.setUser(user);
        comment.setMaintenanceRequest(request);
        comment = commentRepository.save(comment);

        // Test find by maintenance request
        var pageable = PageRequest.of(0, 10);
        var comments = commentRepository.findByMaintenanceRequest(request, pageable);
        assertTrue(comments.getTotalElements() > 0);
    }

    @Test
    void testAttachmentRepository() {
        // Create and save a maintenance request
        User user = new User();
        user.setUsername("uploader");
        user.setPassword("password");
        user.setFullName("Test Uploader");
        user.setEmail("uploader@example.com");
        user = userRepository.save(user);

        MaintenanceRequest request = new MaintenanceRequest();
        request.setTitle("Test Request");
        request.setDescription("Test Description");
        request.setCategory(MaintenanceRequest.RequestCategory.GENERAL);
        request.setRequester(user);
        request = maintenanceRequestRepository.save(request);

        // Create and save an attachment
        Attachment attachment = new Attachment();
        attachment.setFileName("test.jpg");
        attachment.setFileType("image/jpeg");
        attachment.setFileSize(1024L);
        attachment.setFilePath("/uploads/test.jpg");
        attachment.setMaintenanceRequest(request);
        attachment = attachmentRepository.save(attachment);

        // Test find by maintenance request
        var pageable = PageRequest.of(0, 10);
        var attachments = attachmentRepository.findByMaintenanceRequest(request, pageable);
        assertTrue(attachments.getTotalElements() > 0);
    }
} 