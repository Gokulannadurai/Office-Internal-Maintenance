package com.maintenance.service.impl;

import com.maintenance.entity.Comment;
import com.maintenance.entity.MaintenanceRequest;
import com.maintenance.entity.User;
import com.maintenance.repository.CommentRepository;
import com.maintenance.repository.MaintenanceRequestRepository;
import com.maintenance.repository.UserRepository;
import com.maintenance.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MaintenanceRequestRepository maintenanceRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Comment createComment(Comment comment) {
        if (comment.getMaintenanceRequest() == null) {
            throw new RuntimeException("Maintenance request cannot be null");
        }
        if (comment.getUser() == null) {
            throw new RuntimeException("User cannot be null");
        }
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new RuntimeException("Comment content cannot be empty");
        }

        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long id, Comment comment) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!existingComment.getUser().getId().equals(comment.getUser().getId())) {
            throw new RuntimeException("Only the comment author can update the comment");
        }

        existingComment.setContent(comment.getContent());
        existingComment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(existingComment);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByRequest(MaintenanceRequest request, Pageable pageable) {
        return commentRepository.findByMaintenanceRequest(request, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByUser(User user, Pageable pageable) {
        return commentRepository.findByUser(user, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> getLatestCommentsByRequest(MaintenanceRequest request, Pageable pageable) {
        return commentRepository.findLatestCommentsByRequest(request, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> searchCommentsInRequest(MaintenanceRequest request, String searchTerm, Pageable pageable) {
        return commentRepository.searchCommentsInRequest(request, searchTerm, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCommentCountByRequest(MaintenanceRequest request) {
        return commentRepository.countByMaintenanceRequest(request);
    }
} 