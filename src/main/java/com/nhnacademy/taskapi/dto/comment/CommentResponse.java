package com.nhnacademy.taskapi.dto.comment;

public record CommentResponse(
        Long commentId,
        Long taskId,
        String writerUserId,
        String content
) {
}
