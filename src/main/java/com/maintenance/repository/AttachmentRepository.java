package com.maintenance.repository;

import com.maintenance.entity.Attachment;
import com.maintenance.entity.MaintenanceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    
    Page<Attachment> findByMaintenanceRequest(MaintenanceRequest maintenanceRequest, Pageable pageable);
    
    @Query("SELECT a FROM Attachment a WHERE a.maintenanceRequest = :request AND " +
           "LOWER(a.fileName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Attachment> searchAttachmentsInRequest(
            @Param("request") MaintenanceRequest request,
            @Param("searchTerm") String searchTerm,
            Pageable pageable);
    
    @Query("SELECT a FROM Attachment a WHERE a.maintenanceRequest = :request AND a.fileType = :fileType")
    Page<Attachment> findByFileType(
            @Param("request") MaintenanceRequest request,
            @Param("fileType") String fileType,
            Pageable pageable);
    
    @Query("SELECT a FROM Attachment a WHERE a.maintenanceRequest = :request ORDER BY a.fileSize DESC")
    Page<Attachment> findLargestAttachments(@Param("request") MaintenanceRequest request, Pageable pageable);
    
    @Query("SELECT SUM(a.fileSize) FROM Attachment a WHERE a.maintenanceRequest = :request")
    Long getTotalSizeByRequest(@Param("request") MaintenanceRequest request);
    
    @Query("SELECT a FROM Attachment a WHERE a.maintenanceRequest = :request AND a.fileType IN :fileTypes")
    Page<Attachment> findByFileTypes(
            @Param("request") MaintenanceRequest request,
            @Param("fileTypes") List<String> fileTypes,
            Pageable pageable);
} 