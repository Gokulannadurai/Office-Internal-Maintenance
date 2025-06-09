package com.maintenance.controller;

import com.maintenance.dto.MaintenanceRequestDTO;
import com.maintenance.entity.MaintenanceRequest;
import com.maintenance.entity.User;
import com.maintenance.service.MaintenanceRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MaintenanceRequestControllerTest {

    private MaintenanceRequestController maintenanceRequestController;

    @Mock
    private MaintenanceRequestService maintenanceRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        maintenanceRequestController = new MaintenanceRequestController(maintenanceRequestService);
    }

    @Test
    void createRequest_ValidRequest_ReturnsCreatedRequest() {
        // Arrange
        MaintenanceRequestDTO requestDTO = new MaintenanceRequestDTO();
        requestDTO.setTitle("Test Request");
        requestDTO.setDescription("Test Description");
        requestDTO.setPriority("HIGH");

        MaintenanceRequest createdRequest = new MaintenanceRequest();
        createdRequest.setId(1L);
        createdRequest.setTitle(requestDTO.getTitle());
        createdRequest.setDescription(requestDTO.getDescription());
        createdRequest.setPriority(requestDTO.getPriority());

        when(maintenanceRequestService.createRequest(any(MaintenanceRequest.class))).thenReturn(createdRequest);

        // Act
        ResponseEntity<MaintenanceRequestDTO> response = maintenanceRequestController.createRequest(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdRequest.getId(), response.getBody().getId());
        assertEquals(createdRequest.getTitle(), response.getBody().getTitle());
    }

    @Test
    void getAllRequests_ReturnsRequestList() {
        // Arrange
        MaintenanceRequest request1 = new MaintenanceRequest();
        request1.setId(1L);
        request1.setTitle("Request 1");

        MaintenanceRequest request2 = new MaintenanceRequest();
        request2.setId(2L);
        request2.setTitle("Request 2");

        List<MaintenanceRequest> requests = Arrays.asList(request1, request2);
        Page<MaintenanceRequest> requestPage = new PageImpl<>(requests);
        Pageable pageable = PageRequest.of(0, 10);

        when(maintenanceRequestService.getAllRequests(pageable)).thenReturn(requestPage);

        // Act
        ResponseEntity<Page<MaintenanceRequestDTO>> response = maintenanceRequestController.getAllRequests(pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    void getRequestById_ExistingRequest_ReturnsRequest() {
        // Arrange
        Long requestId = 1L;
        MaintenanceRequest request = new MaintenanceRequest();
        request.setId(requestId);
        request.setTitle("Test Request");

        when(maintenanceRequestService.getRequestById(requestId)).thenReturn(Optional.of(request));

        // Act
        ResponseEntity<MaintenanceRequestDTO> response = maintenanceRequestController.getRequestById(requestId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(requestId, response.getBody().getId());
        assertEquals(request.getTitle(), response.getBody().getTitle());
    }

    @Test
    void getRequestById_NonExistingRequest_ReturnsNotFound() {
        // Arrange
        Long requestId = 999L;
        when(maintenanceRequestService.getRequestById(requestId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<MaintenanceRequestDTO> response = maintenanceRequestController.getRequestById(requestId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateRequest_ValidRequest_ReturnsUpdatedRequest() {
        // Arrange
        Long requestId = 1L;
        MaintenanceRequestDTO requestDTO = new MaintenanceRequestDTO();
        requestDTO.setId(requestId);
        requestDTO.setTitle("Updated Request");
        requestDTO.setDescription("Updated Description");

        MaintenanceRequest updatedRequest = new MaintenanceRequest();
        updatedRequest.setId(requestId);
        updatedRequest.setTitle(requestDTO.getTitle());
        updatedRequest.setDescription(requestDTO.getDescription());

        when(maintenanceRequestService.updateRequest(eq(requestId), any(MaintenanceRequest.class)))
                .thenReturn(updatedRequest);

        // Act
        ResponseEntity<MaintenanceRequestDTO> response = maintenanceRequestController.updateRequest(requestId, requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(requestId, response.getBody().getId());
        assertEquals(requestDTO.getTitle(), response.getBody().getTitle());
    }

    @Test
    void updateRequestStatus_ValidStatus_ReturnsUpdatedRequest() {
        // Arrange
        Long requestId = 1L;
        String newStatus = "IN_PROGRESS";
        MaintenanceRequest updatedRequest = new MaintenanceRequest();
        updatedRequest.setId(requestId);
        updatedRequest.setStatus(newStatus);

        when(maintenanceRequestService.updateRequestStatus(requestId, newStatus)).thenReturn(updatedRequest);

        // Act
        ResponseEntity<MaintenanceRequestDTO> response = maintenanceRequestController.updateRequestStatus(requestId, newStatus);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(requestId, response.getBody().getId());
        assertEquals(newStatus, response.getBody().getStatus());
    }

    @Test
    void assignRequest_ValidAssignment_ReturnsUpdatedRequest() {
        // Arrange
        Long requestId = 1L;
        Long assigneeId = 2L;
        MaintenanceRequest updatedRequest = new MaintenanceRequest();
        updatedRequest.setId(requestId);
        User assignee = new User();
        assignee.setId(assigneeId);
        updatedRequest.setAssignee(assignee);

        when(maintenanceRequestService.assignRequest(requestId, assigneeId)).thenReturn(updatedRequest);

        // Act
        ResponseEntity<MaintenanceRequestDTO> response = maintenanceRequestController.assignRequest(requestId, assigneeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(requestId, response.getBody().getId());
        assertEquals(assigneeId, response.getBody().getAssigneeId());
    }

    @Test
    void deleteRequest_ExistingRequest_ReturnsNoContent() {
        // Arrange
        Long requestId = 1L;
        doNothing().when(maintenanceRequestService).deleteRequest(requestId);

        // Act
        ResponseEntity<Void> response = maintenanceRequestController.deleteRequest(requestId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(maintenanceRequestService).deleteRequest(requestId);
    }

    @Test
    void searchRequests_ReturnsMatchingRequests() {
        // Arrange
        String searchTerm = "test";
        MaintenanceRequest request1 = new MaintenanceRequest();
        request1.setId(1L);
        request1.setTitle("Test Request 1");

        MaintenanceRequest request2 = new MaintenanceRequest();
        request2.setId(2L);
        request2.setTitle("Test Request 2");

        List<MaintenanceRequest> requests = Arrays.asList(request1, request2);
        Page<MaintenanceRequest> requestPage = new PageImpl<>(requests);
        Pageable pageable = PageRequest.of(0, 10);

        when(maintenanceRequestService.searchRequests(searchTerm, pageable)).thenReturn(requestPage);

        // Act
        ResponseEntity<Page<MaintenanceRequestDTO>> response = maintenanceRequestController.searchRequests(searchTerm, pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    void getRequestsByStatus_ReturnsFilteredRequests() {
        // Arrange
        String status = "OPEN";
        MaintenanceRequest request1 = new MaintenanceRequest();
        request1.setId(1L);
        request1.setStatus(status);

        MaintenanceRequest request2 = new MaintenanceRequest();
        request2.setId(2L);
        request2.setStatus(status);

        List<MaintenanceRequest> requests = Arrays.asList(request1, request2);
        Page<MaintenanceRequest> requestPage = new PageImpl<>(requests);
        Pageable pageable = PageRequest.of(0, 10);

        when(maintenanceRequestService.getRequestsByStatus(status, pageable)).thenReturn(requestPage);

        // Act
        ResponseEntity<Page<MaintenanceRequestDTO>> response = maintenanceRequestController.getRequestsByStatus(status, pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }
} 