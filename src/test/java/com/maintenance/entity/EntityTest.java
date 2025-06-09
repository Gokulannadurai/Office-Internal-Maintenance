package com.maintenance.entity;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUserEntity() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.getRoles().add("ROLE_USER");

        var violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testMaintenanceRequestEntity() {
        MaintenanceRequest request = new MaintenanceRequest();
        request.setTitle("Test Request");
        request.setDescription("Test Description");
        request.setCategory(MaintenanceRequest.RequestCategory.GENERAL);
        request.setPriority(MaintenanceRequest.RequestPriority.MEDIUM);
        request.setStatus(MaintenanceRequest.RequestStatus.OPEN);

        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testCommentEntity() {
        Comment comment = new Comment();
        comment.setContent("Test Comment");

        var violations = validator.validate(comment);
        assertFalse(violations.isEmpty()); // Should fail due to missing required relationships
    }

    @Test
    void testAttachmentEntity() {
        Attachment attachment = new Attachment();
        attachment.setFileName("test.jpg");
        attachment.setFileType("image/jpeg");
        attachment.setFileSize(1024L);
        attachment.setFilePath("/uploads/test.jpg");

        var violations = validator.validate(attachment);
        assertFalse(violations.isEmpty()); // Should fail due to missing required relationships
    }
} 