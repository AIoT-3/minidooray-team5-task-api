package com.nhnacademy.taskapi.dto.task;

import lombok.Builder;

import java.util.List;

@Builder
public record TaskUpdateRequest(
        String title,
        String content,
        Long milestoneId,
        List<Long> tagIds
) {
}
