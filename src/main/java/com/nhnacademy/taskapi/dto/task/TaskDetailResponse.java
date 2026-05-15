package com.nhnacademy.taskapi.dto.task;

import com.nhnacademy.taskapi.dto.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.dto.tag.TagResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record TaskDetailResponse(
        Long taskId,
        Long projectId,
        String title,
        String content,
        MilestoneResponse milestone,
        List<TagResponse> tags
) {
}
