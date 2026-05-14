package com.nhnacademy.taskapi.dto.task;

import java.util.List;

public record TaskUpdateRequest(
        String title,
        String content,
        Long milestoneId,
        List<Long> tagIds
) {
}
