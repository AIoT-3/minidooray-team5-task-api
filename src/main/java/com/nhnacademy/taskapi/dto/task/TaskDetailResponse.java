package com.nhnacademy.taskapi.dto.task;

import com.nhnacademy.taskapi.dto.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.dto.tag.TagResponse;

import java.util.List;

public record TaskDetailResponse(
        Long taskId,
        Long projectId,
        String title,
        String content,
        MilestoneResponse milestone,
        List<TagResponse> tags
) {
}
