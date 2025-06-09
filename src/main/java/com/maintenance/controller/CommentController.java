package com.maintenance.controller;

import com.maintenance.dto.ApiResponse;
import com.maintenance.dto.CommentDTO;
import com.maintenance.entity.Comment;
import com.maintenance.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentDTO>> createComment(@Valid @RequestBody CommentDTO commentDTO) {
        Comment comment = commentService.createComment(commentDTO.toEntity());
        return ResponseEntity.ok(ApiResponse.success("Comment created successfully", CommentDTO.fromEntity(comment)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentDTO commentDTO) {
        Comment comment = commentService.updateComment(id, commentDTO.toEntity());
        return ResponseEntity.ok(ApiResponse.success("Comment updated successfully", CommentDTO.fromEntity(comment)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.success("Comment deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        return ResponseEntity.ok(ApiResponse.success(CommentDTO.fromEntity(comment)));
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<ApiResponse<Page<CommentDTO>>> getCommentsByRequest(
            @PathVariable Long requestId,
            Pageable pageable) {
        Page<Comment> comments = commentService.getCommentsByRequest(requestId, pageable);
        Page<CommentDTO> commentDTOs = comments.map(CommentDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(commentDTOs));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<CommentDTO>>> getCommentsByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        Page<Comment> comments = commentService.getCommentsByUser(userId, pageable);
        Page<CommentDTO> commentDTOs = comments.map(CommentDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(commentDTOs));
    }

    @GetMapping("/request/{requestId}/latest")
    public ResponseEntity<ApiResponse<Page<CommentDTO>>> getLatestCommentsByRequest(
            @PathVariable Long requestId,
            Pageable pageable) {
        Page<Comment> comments = commentService.getLatestCommentsByRequest(requestId, pageable);
        Page<CommentDTO> commentDTOs = comments.map(CommentDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(commentDTOs));
    }

    @GetMapping("/request/{requestId}/search")
    public ResponseEntity<ApiResponse<Page<CommentDTO>>> searchCommentsInRequest(
            @PathVariable Long requestId,
            @RequestParam String searchTerm,
            Pageable pageable) {
        Page<Comment> comments = commentService.searchCommentsInRequest(requestId, searchTerm, pageable);
        Page<CommentDTO> commentDTOs = comments.map(CommentDTO::fromEntity);
        return ResponseEntity.ok(ApiResponse.success(commentDTOs));
    }

    @GetMapping("/request/{requestId}/count")
    public ResponseEntity<ApiResponse<Long>> getCommentCountByRequest(@PathVariable Long requestId) {
        Long count = commentService.getCommentCountByRequest(requestId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
} 