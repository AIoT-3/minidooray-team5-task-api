package com.nhnacademy.taskapi.dto.comment;

import lombok.Builder;

@Builder
public record CommentResponse(
        Long commentId,
        Long taskId,
        String writerUserId,
        String content
) {
}
