package com.maintenance.repository;

import com.maintenance.entity.MaintenanceRequest;
import com.maintenance.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {
    
    Page<MaintenanceRequest> findByRequester(User requester, Pageable pageable);
    
    Page<MaintenanceRequest> findByAssignedTo(User assignedTo, Pageable pageable);
    
    @Query("SELECT mr FROM MaintenanceRequest mr WHERE " +
           "LOWER(mr.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(mr.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<MaintenanceRequest> searchRequests(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT mr FROM MaintenanceRequest mr WHERE mr.status = :status")
    Page<MaintenanceRequest> findByStatus(@Param("status") MaintenanceRequest.RequestStatus status, Pageable pageable);
    
    @Query("SELECT mr FROM MaintenanceRequest mr WHERE mr.priority = :priority")
    Page<MaintenanceRequest> findByPriority(@Param("priority") MaintenanceRequest.RequestPriority priority, Pageable pageable);
    
    @Query("SELECT mr FROM MaintenanceRequest mr WHERE mr.category = :category")
    Page<MaintenanceRequest> findByCategory(@Param("category") MaintenanceRequest.RequestCategory category, Pageable pageable);
    
    @Query("SELECT mr FROM MaintenanceRequest mr WHERE mr.createdAt BETWEEN :startDate AND :endDate")
    Page<MaintenanceRequest> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
    
    @Query("SELECT COUNT(mr) FROM MaintenanceRequest mr WHERE mr.status = :status")
    long countByStatus(@Param("status") MaintenanceRequest.RequestStatus status);
    
    @Query("SELECT mr FROM MaintenanceRequest mr WHERE mr.assignedTo IS NULL")
    Page<MaintenanceRequest> findUnassignedRequests(Pageable pageable);
    
    @Query("SELECT mr FROM MaintenanceRequest mr WHERE mr.requester = :user OR mr.assignedTo = :user")
    Page<MaintenanceRequest> findUserRelatedRequests(@Param("user") User user, Pageable pageable);
} 