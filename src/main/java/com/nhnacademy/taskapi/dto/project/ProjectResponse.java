package com.nhnacademy.taskapi.dto.project;

import com.nhnacademy.taskapi.entity.ProjectStatus;
import lombok.Builder;

@Builder
public record ProjectResponse(
        Long projectId,
        String name,
        ProjectStatus status,
        boolean admin
) {
}
