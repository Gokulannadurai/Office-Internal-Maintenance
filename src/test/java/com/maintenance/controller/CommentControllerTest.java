package com.maintenance.controller;

import com.maintenance.dto.CommentDTO;
import com.maintenance.entity.Comment;
import com.maintenance.entity.MaintenanceRequest;
import com.maintenance.entity.User;
import com.maintenance.service.CommentService;
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

class CommentControllerTest {

    private CommentController commentController;

    @Mock
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentController = new CommentController(commentService);
    }

    @Test
    void createComment_ValidComment_ReturnsCreatedComment() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("Test Comment");
        commentDTO.setRequestId(1L);

        Comment createdComment = new Comment();
        createdComment.setId(1L);
        createdComment.setContent(commentDTO.getContent());
        MaintenanceRequest request = new MaintenanceRequest();
        request.setId(1L);
        createdComment.setRequest(request);

        when(commentService.createComment(any(Comment.class))).thenReturn(createdComment);

        // Act
        ResponseEntity<CommentDTO> response = commentController.createComment(commentDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdComment.getId(), response.getBody().getId());
        assertEquals(createdComment.getContent(), response.getBody().getContent());
    }

    @Test
    void getCommentById_ExistingComment_ReturnsComment() {
        // Arrange
        Long commentId = 1L;
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setContent("Test Comment");

        when(commentService.getCommentById(commentId)).thenReturn(Optional.of(comment));

        // Act
        ResponseEntity<CommentDTO> response = commentController.getCommentById(commentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(commentId, response.getBody().getId());
        assertEquals(comment.getContent(), response.getBody().getContent());
    }

    @Test
    void getCommentById_NonExistingComment_ReturnsNotFound() {
        // Arrange
        Long commentId = 999L;
        when(commentService.getCommentById(commentId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<CommentDTO> response = commentController.getCommentById(commentId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateComment_ValidComment_ReturnsUpdatedComment() {
        // Arrange
        Long commentId = 1L;
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentId);
        commentDTO.setContent("Updated Comment");

        Comment updatedComment = new Comment();
        updatedComment.setId(commentId);
        updatedComment.setContent(commentDTO.getContent());

        when(commentService.updateComment(eq(commentId), any(Comment.class))).thenReturn(updatedComment);

        // Act
        ResponseEntity<CommentDTO> response = commentController.updateComment(commentId, commentDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(commentId, response.getBody().getId());
        assertEquals(commentDTO.getContent(), response.getBody().getContent());
    }

    @Test
    void deleteComment_ExistingComment_ReturnsNoContent() {
        // Arrange
        Long commentId = 1L;
        doNothing().when(commentService).deleteComment(commentId);

        // Act
        ResponseEntity<Void> response = commentController.deleteComment(commentId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(commentService).deleteComment(commentId);
    }

    @Test
    void getCommentsByRequest_ReturnsCommentList() {
        // Arrange
        Long requestId = 1L;
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setContent("Comment 1");

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setContent("Comment 2");

        List<Comment> comments = Arrays.asList(comment1, comment2);
        Page<Comment> commentPage = new PageImpl<>(comments);
        Pageable pageable = PageRequest.of(0, 10);

        when(commentService.getCommentsByRequest(any(MaintenanceRequest.class), eq(pageable)))
                .thenReturn(commentPage);

        // Act
        ResponseEntity<Page<CommentDTO>> response = commentController.getCommentsByRequest(requestId, pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    void searchCommentsInRequest_ReturnsMatchingComments() {
        // Arrange
        Long requestId = 1L;
        String searchTerm = "test";
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setContent("Test Comment 1");

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setContent("Test Comment 2");

        List<Comment> comments = Arrays.asList(comment1, comment2);
        Page<Comment> commentPage = new PageImpl<>(comments);
        Pageable pageable = PageRequest.of(0, 10);

        when(commentService.searchCommentsInRequest(any(MaintenanceRequest.class), eq(searchTerm), eq(pageable)))
                .thenReturn(commentPage);

        // Act
        ResponseEntity<Page<CommentDTO>> response = commentController.searchCommentsInRequest(requestId, searchTerm, pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    void getCommentsByUser_ReturnsUserComments() {
        // Arrange
        Long userId = 1L;
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setContent("User Comment 1");

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setContent("User Comment 2");

        List<Comment> comments = Arrays.asList(comment1, comment2);
        Page<Comment> commentPage = new PageImpl<>(comments);
        Pageable pageable = PageRequest.of(0, 10);

        when(commentService.getCommentsByUser(any(User.class), eq(pageable))).thenReturn(commentPage);

        // Act
        ResponseEntity<Page<CommentDTO>> response = commentController.getCommentsByUser(userId, pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    void getCommentCountByRequest_ReturnsCount() {
        // Arrange
        Long requestId = 1L;
        long expectedCount = 5L;

        when(commentService.getCommentCountByRequest(any(MaintenanceRequest.class))).thenReturn(expectedCount);

        // Act
        ResponseEntity<Long> response = commentController.getCommentCountByRequest(requestId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedCount, response.getBody());
    }
} 