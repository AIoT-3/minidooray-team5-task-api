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

    // 프로젝트 내 테스크의 모든 댓글 조회
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getCommentsByTask(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @RequestHeader("X-User-Id") String userId
    ) {
        List<CommentResponse> resp=commentService.getCommentsByTask(projectId, userId, taskId);

        return ResponseEntity.ok(resp);
    }

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CommentCreateRequest req
    ) {
        CommentResponse resp=commentService.createComment(projectId, userId, taskId, req);

        return ResponseEntity.ok(resp);
    }

    // 댓글 단건 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getComment(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @PathVariable("commentId") Long commentId,
            @RequestHeader("X-User-Id") String userId
    ) {
        CommentResponse resp=commentService.getComment(projectId, userId, taskId, commentId);

        return ResponseEntity.ok(resp);
    }

    // 댓글 수정
    @PostMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @PathVariable("commentId") Long commentId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CommentUpdateRequest req
    ) {
        CommentResponse resp=commentService.updateComment(projectId, userId, taskId, commentId, req);

        return ResponseEntity.ok(resp);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @PathVariable("commentId") Long commentId,
            @RequestHeader("X-User-Id") String userId
    ) {
        commentService.deleteComment(projectId, userId, taskId, commentId);

        return ResponseEntity.noContent().build();
    }
}
