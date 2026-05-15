package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.comment.CommentCreateRequest;
import com.nhnacademy.taskapi.dto.comment.CommentResponse;
import com.nhnacademy.taskapi.dto.comment.CommentUpdateRequest;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getCommentsByTask(Long projectId, String userId, Long taskId);

    CommentResponse createComment(Long projectId, String userId, Long taskId, CommentCreateRequest req);

    CommentResponse getComment(Long projectId, String userId, Long taskId, Long commentId);

    CommentResponse updateComment(Long projectId, String userId, Long taskId, Long commentId, CommentUpdateRequest req);

    void deleteComment(Long projectId, String userId, Long taskId, Long commentId);
}
