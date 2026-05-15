package com.nhnacademy.taskapi.dto.milestone;

import com.nhnacademy.taskapi.entity.MilestoneStatus;

public record MilestoneUpdateRequest(
        String name,
        MilestoneStatus status
) {
}
