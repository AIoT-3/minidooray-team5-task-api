package com.nhnacademy.taskapi.dto.task;

public record TaskCreateRequest(
        String title,
        String content,
        Long projectId
) {
}
