package com.maintenance.service;

import com.maintenance.entity.Comment;
import com.maintenance.entity.MaintenanceRequest;
import com.maintenance.entity.User;
import com.maintenance.repository.CommentRepository;
import com.maintenance.repository.MaintenanceRequestRepository;
import com.maintenance.repository.UserRepository;
import com.maintenance.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MaintenanceRequestRepository maintenanceRequestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment testComment;
    private MaintenanceRequest testRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setFullName("Test User");

        testRequest = new MaintenanceRequest();
        testRequest.setId(1L);
        testRequest.setTitle("Test Request");
        testRequest.setDescription("Test Description");

        testComment = new Comment();
        testComment.setId(1L);
        testComment.setContent("Test Comment");
        testComment.setUser(testUser);
        testComment.setMaintenanceRequest(testRequest);
    }

    @Test
    void createComment_Success() {
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        Comment createdComment = commentService.createComment(testComment);

        assertNotNull(createdComment);
        assertEquals(testComment.getContent(), createdComment.getContent());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void createComment_NullRequest() {
        testComment.setMaintenanceRequest(null);

        assertThrows(RuntimeException.class, () -> commentService.createComment(testComment));
    }

    @Test
    void createComment_NullUser() {
        testComment.setUser(null);

        assertThrows(RuntimeException.class, () -> commentService.createComment(testComment));
    }

    @Test
    void createComment_EmptyContent() {
        testComment.setContent("");

        assertThrows(RuntimeException.class, () -> commentService.createComment(testComment));
    }

    @Test
    void updateComment_Success() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        Comment updatedComment = commentService.updateComment(1L, testComment);

        assertNotNull(updatedComment);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void updateComment_NotFound() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commentService.updateComment(1L, testComment));
    }

    @Test
    void updateComment_UnauthorizedUser() {
        User differentUser = new User();
        differentUser.setId(2L);
        testComment.setUser(differentUser);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(testComment));

        assertThrows(RuntimeException.class, () -> commentService.updateComment(1L, testComment));
    }

    @Test
    void getCommentsByRequest_Success() {
        Page<Comment> commentPage = new PageImpl<>(Arrays.asList(testComment));
        when(commentRepository.findByMaintenanceRequest(any(MaintenanceRequest.class), any(PageRequest.class)))
                .thenReturn(commentPage);

        Page<Comment> foundComments = commentService.getCommentsByRequest(testRequest, PageRequest.of(0, 10));

        assertNotNull(foundComments);
        assertEquals(1, foundComments.getTotalElements());
    }

    @Test
    void getLatestCommentsByRequest_Success() {
        Page<Comment> commentPage = new PageImpl<>(Arrays.asList(testComment));
        when(commentRepository.findLatestCommentsByRequest(any(MaintenanceRequest.class), any(PageRequest.class)))
                .thenReturn(commentPage);

        Page<Comment> foundComments = commentService.getLatestCommentsByRequest(testRequest, PageRequest.of(0, 10));

        assertNotNull(foundComments);
        assertEquals(1, foundComments.getTotalElements());
    }

    @Test
    void searchCommentsInRequest_Success() {
        Page<Comment> commentPage = new PageImpl<>(Arrays.asList(testComment));
        when(commentRepository.searchCommentsInRequest(any(MaintenanceRequest.class), anyString(), any(PageRequest.class)))
                .thenReturn(commentPage);

        Page<Comment> foundComments = commentService.searchCommentsInRequest(testRequest, "test", PageRequest.of(0, 10));

        assertNotNull(foundComments);
        assertEquals(1, foundComments.getTotalElements());
    }

    @Test
    void getCommentCountByRequest_Success() {
        when(commentRepository.countByMaintenanceRequest(any(MaintenanceRequest.class))).thenReturn(5L);

        long count = commentService.getCommentCountByRequest(testRequest);

        assertEquals(5L, count);
    }
} 