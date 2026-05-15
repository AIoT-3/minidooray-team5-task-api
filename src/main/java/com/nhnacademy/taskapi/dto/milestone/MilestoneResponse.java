package com.nhnacademy.taskapi.dto.milestone;

import com.nhnacademy.taskapi.entity.MilestoneStatus;
import lombok.Builder;

@Builder
public record MilestoneResponse(
        Long milestoneId,
        String name,
        MilestoneStatus status
) {
}
