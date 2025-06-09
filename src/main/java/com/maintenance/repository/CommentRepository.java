package com.maintenance.repository;

import com.maintenance.entity.Comment;
import com.maintenance.entity.MaintenanceRequest;
import com.maintenance.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    Page<Comment> findByMaintenanceRequest(MaintenanceRequest maintenanceRequest, Pageable pageable);
    
    Page<Comment> findByUser(User user, Pageable pageable);
    
    @Query("SELECT c FROM Comment c WHERE c.maintenanceRequest = :request ORDER BY c.createdAt DESC")
    Page<Comment> findLatestCommentsByRequest(@Param("request") MaintenanceRequest request, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.maintenanceRequest = :request")
    long countByMaintenanceRequest(@Param("request") MaintenanceRequest request);
    
    @Query("SELECT c FROM Comment c WHERE c.maintenanceRequest = :request AND " +
           "LOWER(c.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Comment> searchCommentsInRequest(
            @Param("request") MaintenanceRequest request,
            @Param("searchTerm") String searchTerm,
            Pageable pageable);
} 