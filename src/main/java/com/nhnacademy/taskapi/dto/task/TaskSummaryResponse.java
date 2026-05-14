package com.nhnacademy.taskapi.dto.task;

import java.util.List;

public record TaskSummaryResponse(
        Long taskId,
        String title,
        String milestoneName,
        List<String> tags,
        long commentCount
) {
}
