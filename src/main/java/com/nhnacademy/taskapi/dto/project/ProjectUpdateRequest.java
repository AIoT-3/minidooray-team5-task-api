package com.nhnacademy.taskapi.dto.project;

import com.nhnacademy.taskapi.entity.ProjectStatus;

public record ProjectUpdateRequest(
        String name,
        ProjectStatus status
) {}
