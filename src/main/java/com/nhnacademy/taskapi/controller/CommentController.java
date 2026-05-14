package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.comment.CommentCreateRequest;
import com.nhnacademy.taskapi.dto.comment.CommentResponse;
import com.nhnacademy.taskapi.dto.comment.CommentUpdateRequest;
import com.nhnacademy.taskapi.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getCommentsByTask(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @RequestHeader("X-User-Id") String userId
    ) {

        return null;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CommentCreateRequest req
    ) {

        return null;
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getComment(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @PathVariable("commentId") Long commentId,
            @RequestHeader("X-User-Id") String userId
    ) {

        return null;
    }

    @PostMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @PathVariable("commentId") Long commentId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CommentUpdateRequest req
    ) {

        return null;
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @PathVariable("commentId") Long commentId,
            @RequestHeader("X-User-Id") String userId
    ) {

        return null;
    }
}
