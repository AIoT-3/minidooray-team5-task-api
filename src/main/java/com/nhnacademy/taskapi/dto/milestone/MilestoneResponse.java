package com.nhnacademy.taskapi.dto.milestone;

import lombok.Builder;

@Builder
public record MilestoneResponse(
        Long milestoneId,
        String name
) {
}
